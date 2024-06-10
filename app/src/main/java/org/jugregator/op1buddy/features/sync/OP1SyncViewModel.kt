package org.jugregator.op1buddy.features.sync

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import me.jahnen.libaums.core.fs.FileSystem
import me.jahnen.libaums.core.fs.UsbFile
import me.jahnen.libaums.core.fs.UsbFileStreamFactory
import org.jugregator.op1buddy.LibAUMSDemo
import org.jugregator.op1buddy.OP1State
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

private const val BUFFER_SIZE = 512

class OP1SyncViewModel : ViewModel() {
    private lateinit var backupDir: File
    private var deviceState = SyncScreenStateDeprecated()

    private val _stateFlow = MutableStateFlow(BackupScreenState())
    val stateFlow: StateFlow<BackupScreenState> = _stateFlow

    lateinit var aums: LibAUMSDemo

    fun init(context: Context) {
        aums = LibAUMSDemo(
            context,
            connectedCallback = { fs ->
                deviceState = deviceState.copy(fs = fs, error = null)
                _stateFlow.update { it.copy(connected = OP1ConnectionState.Connecting) }
            },
            disconnectedCallback = {
                deviceState = deviceState.copy(fs = null, error = null, fsState = null)
                _stateFlow.update { it.copy(connected = OP1ConnectionState.Disconnected) }
            },
            errorCallback = { error ->
                deviceState = deviceState.copy(error = error)
                _stateFlow.update { it.copy(connected = OP1ConnectionState.Connecting, error = error) }
            },
            fsInitCallback = { state ->
                deviceState = deviceState.copy(error = null, fsState = state)
                _stateFlow.update { it.copy(connected = OP1ConnectionState.Connected) }
            }
        )
        aums.initReceiver()

        backupDir = File(context.filesDir, "op1backup")
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val singleThreadContext = newSingleThreadContext("usb access")

    fun copyMultipleFilesFromUsb(
        usbFiles: List<UsbFile>,
        fs: FileSystem,
        targetFiles: List<File>,
        onProgress: (Float) -> Unit
    ) {
        if (usbFiles.size != targetFiles.size) {
            error("usbFiles and targetFiles must be same length")
        }

        val fullLength = usbFiles.sumOf { it.length }
        var currentLength = 0L
        for ((usbFile, file) in usbFiles.zip(targetFiles)) {
            copyFileFromUsb(usbFile, fs, file) { progress ->
                currentLength += progress
                onProgress((currentLength.toFloat() / fullLength.toFloat()))
            }
        }
    }

    private fun copyFileFromUsb(sourceUsbFile: UsbFile, fs: FileSystem, targetFile: File, onProgress: (Long) -> Unit) {
        //TODO: fix coroutines

//        val fileSize = sourceUsbFile.length // singleThread
        val outputStream = BufferedOutputStream(targetFile.outputStream()) // IO
        val input = UsbFileStreamFactory.createBufferedInputStream(sourceUsbFile, fs) // singleThread
        val buffer = ByteArray(fs.chunkSize)
        var fullSize = 0L
        var resultSize = input.read(buffer) // singleThread

        while (resultSize != -1) {
            outputStream.write(buffer) // IO
            fullSize += resultSize
            onProgress(resultSize.toLong())
            resultSize = input.read(buffer) // singleThread
        }
        outputStream.close() // IO

        println("$sourceUsbFile to $targetFile copied")
    }

    fun copyFileToUsb(targetUsbFile: UsbFile, fs: FileSystem, sourceFile: File) {
        viewModelScope.launch {
            _stateFlow.update { it.copy(nowCopying = true) }
            withContext(Dispatchers.IO) {
                val fileSize = sourceFile.length()
                val output = UsbFileStreamFactory.createBufferedOutputStream(targetUsbFile, fs)
                val input = BufferedInputStream(sourceFile.inputStream())

                val buffer = ByteArray(fs.chunkSize)
                var fullSize = 0L
                var resultSize = input.read(buffer)

                var percent = 0
                var newPercent: Int

                while (resultSize != -1) {
                    output.write(buffer, 0, resultSize)

                    fullSize += resultSize

                    newPercent = ((fullSize.toFloat() / fileSize.toFloat()) * 100).toInt()
                    resultSize = input.read(buffer)
                    yield()

                    if (newPercent != percent) {
                        percent = newPercent
                        deviceState = deviceState.copy(bytesWasWritePercent = percent)
                    }
                }
                output.close()
                input.close()
            }
            _stateFlow.update { it.copy(nowCopying = false) }
        }
    }

    fun backupDevice() {
        // Clear previous backup
        clearBackupDir()

        val backupInfo = stateFlow.value.backupInfo
        val fsState = deviceState.fsState
        val fs = deviceState.fs

        val backupUsbFiles = mutableListOf<UsbFile>()
        val backupTargetFiles = mutableListOf<File>()

        if (fsState != null && fs != null) {
            if (backupInfo.synths) {
                //copy synths
                for ((synthIndex, synthUsbFile) in fsState.synths.withIndex()) {
                    backupUsbFiles.add(synthUsbFile)
                    val localSynthFile = File(backupDir, "synth_${synthIndex + 1}.aif")
                    backupTargetFiles.add(localSynthFile)
                }
            }

            if (backupInfo.drumkits) {
                // copy drumkits
                for ((drumkitIndex, drumkitUsbFile) in fsState.drums.withIndex()) {
                    backupUsbFiles.add(drumkitUsbFile)
                    val localDrumkitFile = File(backupDir, "drum_${drumkitIndex + 1}.aif")
                    backupTargetFiles.add(localDrumkitFile)
                }
            }

            for ((tape, isNeedToBackup) in backupInfo.tapes) {
                if (isNeedToBackup) {
                    val tapeIndex = tape.index
                    val localTapeFile = File(backupDir, "tape_${tapeIndex + 1}.aif")
                    val sourceUsbTapeFile = fsState.tracks[tapeIndex]
                    backupUsbFiles.add(sourceUsbTapeFile)
                    backupTargetFiles.add(localTapeFile)
                }
            }

            viewModelScope.launch {
                withContext(singleThreadContext) {
                    _stateFlow.update { it.copy(nowCopying = true) }
                    copyMultipleFilesFromUsb(backupUsbFiles, fs, backupTargetFiles) { progress ->
                        _stateFlow.update { it.copy(progress = progress) }
                    }
                    _stateFlow.update { it.copy(nowCopying = false, backupInfo = BackupInfo()) }
                }
            }
        }
    }

    private fun clearBackupDir() {
        val filesToDelete = backupDir.listFiles() ?: arrayOf()
        for (file in filesToDelete) {
            file.delete()
        }
    }

    fun restoreDevice() {

    }

    fun chooseNewBackupFile() {

    }

    fun createBackup(filesDir: File, outputZipFileOutputStream: OutputStream) {
        _stateFlow.update { it.copy(nowCopying = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val files = (filesDir.listFiles() ?: arrayOf<File>()).map { it.name }

                    var origin: BufferedInputStream? = null

                    val dest = outputZipFileOutputStream
                    val out = ZipOutputStream(BufferedOutputStream(dest))

                    val data = ByteArray(BUFFER_SIZE)
                    for (filename in files) {
                        val file = File(filesDir, filename)
                        val fi = FileInputStream(file)
                        origin = BufferedInputStream(fi, BUFFER_SIZE)

                        val entry = ZipEntry(filename.substring(filename.lastIndexOf("/") + 1))
                        out.putNextEntry(entry)

                        var count = origin.read(data, 0, BUFFER_SIZE)

                        while (count != -1) {
                            out.write(data, 0, count)
                            count = origin.read(data, 0, BUFFER_SIZE)
                        }
                        origin.close()
                    }
                    out.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        _stateFlow.update { it.copy(nowCopying = false) }
    }

    override fun onCleared() {
        super.onCleared()
        aums.destroyReceiver()
        aums.closeDevices()
    }

    fun updateBackupInfo(backupInfo: BackupInfo) {
        _stateFlow.update { it.copy(backupInfo = backupInfo) }
    }

    fun onBackupDirSelected(context: Context, rootDir: Uri) {
        val contentResolver = context.contentResolver
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        // Check for the freshest data.
        contentResolver.takePersistableUriPermission(rootDir, takeFlags)

        val selectedFile = DocumentFile.fromSingleUri(context, rootDir)
        if (selectedFile == null) {
            Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show()
            return
        }

        val writer = contentResolver.openOutputStream(selectedFile.uri, "wt")

        writer?.let { outputStream ->
            createBackup(backupDir, outputStream)
        }
    }
}

data class SyncScreenStateDeprecated(
    val fs: FileSystem? = null,
    val error: String? = null,
    val fsState: OP1State? = null,
    val bytesWasReadPercent: Int = 0,
    val bytesWasWritePercent: Int = 0,
    val nowCopying: Boolean = false
)

data class BackupScreenState(
    val connected: OP1ConnectionState = OP1ConnectionState.Disconnected,
    val error: String = "",
    val nowCopying: Boolean = false,
    val progress: Float = 0f,
    val backupInfo: BackupInfo = BackupInfo(),
)

enum class OP1ConnectionState {
    Disconnected,
    Connecting,
    Connected
}

data class BackupInfo(
    val tapes: SnapshotStateList<Pair<OP1Resource.Tape, Boolean>> =
        (0..<TAPES_COUNT).map { index -> OP1Resource.Tape(index) to false }.toList().toMutableStateList(),
    val synths: Boolean = false,
    val drumkits: Boolean = false,
    val albums: SnapshotStateList<Pair<OP1Resource.Album, Boolean>> = mutableStateListOf(
        OP1Resource.Album(AlbumSide.SideA) to false,
        OP1Resource.Album(AlbumSide.SideB) to false,
    )
)

sealed class OP1Resource {
    class Tape(val index: Int) : OP1Resource()
    class Synth(val index: Int) : OP1Resource()
    class Drumkit(val index: Int) : OP1Resource()
    class Album(val side: AlbumSide) : OP1Resource()
}

enum class AlbumSide(index: Int) {
    SideA(0),
    SideB(1)
}