package org.jugregator.op1buddy.features.sync

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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

class OP1SyncViewModel : ViewModel() {
    private lateinit var backupDir: File
//    private lateinit var demoFileForCopyToUsb: File
//    private lateinit var demoFileToCopyFromUsb: File
//    private val _stateFlow: MutableStateFlow<SyncScreenStateDeprecated> = MutableStateFlow(SyncScreenStateDeprecated(null, null, null))
    private var deviceState = SyncScreenStateDeprecated()

    //val stateFlow: StateFlow<SyncScreenStateDeprecated> = _stateFlow

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

//        demoFileToCopyFromUsb = File(context.filesDir, "instrument_01.aiff")

//        demoFileForCopyToUsb = File(context.filesDir, "instrument_01.aiff")
        backupDir = File(context.filesDir, "op1backup")
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val singleThreadContext = newSingleThreadContext("usb access")

    fun copyFileFromUsb(sourceUsbFile: UsbFile, fs: FileSystem, targetFile: File) {
        //TODO: fix coroutines
        viewModelScope.launch {
            _stateFlow.update { it.copy(nowCopying = true) }
            withContext(singleThreadContext) {
                val fileSize = sourceUsbFile.length // singleThread
                val outputStream = BufferedOutputStream(targetFile.outputStream()) // IO
                val input = UsbFileStreamFactory.createBufferedInputStream(sourceUsbFile, fs) // singleThread
                val buffer = ByteArray(fs.chunkSize)
                var fullSize = 0L
                var resultSize = input.read(buffer) // singleThread

                var percent = 0
                var newPercent = -1
                while (resultSize != -1) {
                    outputStream.write(buffer) // IO
                    fullSize += resultSize

                    newPercent = ((fullSize.toFloat() / fileSize.toFloat()) * 100).toInt()
                    resultSize = input.read(buffer) // singleThread
                    yield()

                    if (newPercent != percent) {
                        percent = newPercent
                        deviceState = deviceState.copy(bytesWasReadPercent = percent)
                        //TODO: report progress
                    }

                }
                outputStream.close() // IO
            }
            _stateFlow.update { it.copy(nowCopying = false) }
        }
    }

    fun copyFileToUsb(targetUsbFile: UsbFile, fs: FileSystem, sourceFile: File) {
        viewModelScope.launch {
            _stateFlow.update { it.copy(nowCopying = true) }
            withContext(Dispatchers.IO) {
//                val fileName = usbFile.name
//                usbFile.delete()
                //val usbFile = usbFile.parent!!.createFile(usbFile.name)
                val fileSize = sourceFile.length()
                val output = UsbFileStreamFactory.createBufferedOutputStream(targetUsbFile, fs)
                val input = BufferedInputStream(sourceFile.inputStream())

                val buffer = ByteArray(fs.chunkSize)
                var fullSize = 0L
                var resultSize = input.read(buffer)

                var percent = 0
                var newPercent = -1

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
        //TODO: rework this with coroutines, state and else
        val backupInfo = stateFlow.value.backupInfo
        val fsState = deviceState.fsState
        val fs = deviceState.fs
        if (fsState != null && fs != null) {
            if (backupInfo.synths) {
                //copy synths
                for ((synthIndex, synthUsbFile) in fsState.synths.withIndex()) {
                    val localSynthFile = File(backupDir, "synth_${synthIndex + 1}.aif")
                    copyFileFromUsb(sourceUsbFile = synthUsbFile, fs, localSynthFile)
                    println("$localSynthFile copied")
                }
            }

            if (backupInfo.drumkits) {
                // copy drumkits
                for ((drumkitIndex, drumkitUsbFile) in fsState.drums.withIndex()) {
                    val localDrumkitFile = File(backupDir, "drum_${drumkitIndex + 1}.aif")
                    copyFileFromUsb(sourceUsbFile = drumkitUsbFile, fs, localDrumkitFile)
                    println("$localDrumkitFile copied")
                }
            }
        }
    }

    fun restoreDevice() {

    }

    override fun onCleared() {
        super.onCleared()
        aums.destroyReceiver()
        aums.closeDevices()
    }

    fun updateBackupInfo(backupInfo: BackupInfo) {
        _stateFlow.update { it.copy(backupInfo = backupInfo) }
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
    class Tape(val index: Int): OP1Resource()
    class Synth(val index: Int): OP1Resource()
    class Drumkit(val index: Int): OP1Resource()
    class Album(val side: AlbumSide): OP1Resource()
}

enum class AlbumSide(index: Int) {
    SideA(0),
    SideB(1)
}