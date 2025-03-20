package org.jugregator.op1buddy.features.sync

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import me.jahnen.libaums.core.fs.FileSystem
import me.jahnen.libaums.core.fs.UsbFile
import org.jugregator.op1buddy.AumsUsbController
import org.jugregator.op1buddy.OP1State
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.data.project.LocalFileRepository
import org.jugregator.op1buddy.features.projects.SyncRoute
import org.jugregator.op1buddy.data.project.BackupRepository
import org.jugregator.op1buddy.features.sync.data.UsbFileRepository
import java.io.File
import java.io.OutputStream

const val TAPES_COUNT = 4

class OP1SyncViewModel(
    savedStateHandle: SavedStateHandle,
    private val usbFileRepository: UsbFileRepository,
    private val backupRepository: BackupRepository,
    private val localFileRepository: LocalFileRepository,
    private val projectsRepository: ProjectsRepository
) : ViewModel() {
    private var backupDirPath: String = ""
    private lateinit var backupDir: File
    private var deviceState = DeviceState()

    private val _backupStateFlow = MutableStateFlow(BackupScreenState())
    val backupStateFlow: StateFlow<BackupScreenState> = _backupStateFlow

    private val _restoreStateFlow = MutableStateFlow(RestoreScreenState())
    val restoreStateFlow: StateFlow<RestoreScreenState> = _restoreStateFlow

    private lateinit var aums: AumsUsbController

    private val route = savedStateHandle.toRoute<SyncRoute>()

    fun init(context: Context) {
        Log.w("SYNC ROUTE", route.toString())
        aums = AumsUsbController(
            context,
            connectedCallback = { fs ->
                deviceState = deviceState.copy(fs = fs, error = null)
                _backupStateFlow.update { it.copy(connected = OP1ConnectionState.Connecting) }
                _restoreStateFlow.update { it.copy(connected = OP1ConnectionState.Connecting) }
            },
            disconnectedCallback = {
                deviceState = deviceState.copy(fs = null, error = null, fsState = null)
                _backupStateFlow.update { it.copy(connected = OP1ConnectionState.Disconnected) }
                _restoreStateFlow.update { it.copy(connected = OP1ConnectionState.Disconnected) }
            },
            errorCallback = { error ->
                deviceState = deviceState.copy(error = error)
                _backupStateFlow.update { it.copy(connected = OP1ConnectionState.Connecting, error = error) }
                _restoreStateFlow.update { it.copy(connected = OP1ConnectionState.Connecting, error = error) }
            },
            fsInitCallback = { state ->
                deviceState = deviceState.copy(error = null, fsState = state)
                _backupStateFlow.update { it.copy(connected = OP1ConnectionState.Connected) }
                _restoreStateFlow.update { it.copy(connected = OP1ConnectionState.Connected) }
            }
        )
        aums.initReceiver()


        viewModelScope.launch {
            val project = projectsRepository.readProject(route.projectId)
            backupDirPath = project?.backupDir ?: ""
            backupDir = File(context.filesDir, "op1backup/${backupDirPath}")
            if (!backupDir.exists()) {
                backupDir.mkdirs()
            }

            refreshLocalBackupInfo()
        }
    }

    fun deInit() {
        aums.destroyReceiver()
        aums.closeDevices()
    }

    private fun refreshLocalBackupInfo() {
        val savedBackupInfo = localFileRepository.readBackupInfo(backupDirPath)
        _restoreStateFlow.update { it.copy(backupInfo = savedBackupInfo) }
    }

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val singleThreadContext = newSingleThreadContext("usb access")

    fun backupDevice() {
        // Clear previous backup
        clearBackupDir()

        val backupInfo = backupStateFlow.value.backupInfo
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
                    _backupStateFlow.update { it.copy(nowCopying = true) }
                    usbFileRepository.copyMultipleFilesFromUsb(backupUsbFiles, fs, backupTargetFiles) { progress ->
                        _backupStateFlow.update { it.copy(progress = progress) }
                    }
                    _backupStateFlow.update { it.copy(nowCopying = false, backupInfo = BackupInfo()) }
                    refreshLocalBackupInfo()
                }
            }
        }
    }

    fun restoreDevice() {
        val backupInfo = restoreStateFlow.value.backupInfo
        val fsState = deviceState.fsState
        val fs = deviceState.fs

        val backupLocalFiles = mutableListOf<File>()
        val backupTargetUsbFiles = mutableListOf<UsbFile>()

        if (fsState != null && fs != null) {
            if (backupInfo.synths) {
                //copy synths
                for ((synthIndex, synthUsbFile) in fsState.synths.withIndex()) {
                    val localSynthFile = File(backupDir, "synth_${synthIndex + 1}.aif")
                    backupLocalFiles.add(localSynthFile)
                    backupTargetUsbFiles.add(synthUsbFile)
                }
            }

            if (backupInfo.drumkits) {
                // copy drumkits
                for ((drumkitIndex, drumkitUsbFile) in fsState.drums.withIndex()) {
                    val localDrumkitFile = File(backupDir, "drum_${drumkitIndex + 1}.aif")
                    backupLocalFiles.add(localDrumkitFile)
                    backupTargetUsbFiles.add(drumkitUsbFile)
                }
            }


            for ((tape, isNeedToBackup) in backupInfo.tapes) {
                if (isNeedToBackup) {
                    val tapeIndex = tape.index
                    val localTapeFile = File(backupDir, "tape_${tapeIndex + 1}.aif")
                    val sourceUsbTapeFile = fsState.tracks[tapeIndex]
                    backupLocalFiles.add(localTapeFile)
                    backupTargetUsbFiles.add(sourceUsbTapeFile)
                }
            }

            viewModelScope.launch {
                withContext(singleThreadContext) {
                    _restoreStateFlow.update { it.copy(nowCopying = true) }
                    usbFileRepository.copyMultipleFilesToUsb(backupTargetUsbFiles, fs, backupLocalFiles) { progress ->
                        _restoreStateFlow.update { it.copy(progress = progress) }
                    }
                    _restoreStateFlow.update { it.copy(nowCopying = false, backupInfo = BackupInfo()) }
                    refreshLocalBackupInfo()
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

    private fun createBackupExport(filesDir: File, outputZipFileOutputStream: OutputStream) {
        _backupStateFlow.update { it.copy(nowCopying = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                backupRepository.compressBackup(filesDir, outputZipFileOutputStream)
            }
        }
        _backupStateFlow.update { it.copy(nowCopying = false) }
    }

    fun updateBackupInfo(backupInfo: BackupInfo) {
        _backupStateFlow.update { it.copy(backupInfo = backupInfo) }
    }

    fun updateRestoreInfo(restoreInfo: BackupInfo) {
        _restoreStateFlow.update { it.copy(backupInfo = restoreInfo) }
    }

    fun onBackupExportDirSelected(context: Context, rootDir: Uri) {
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
            createBackupExport(backupDir, outputStream)
        }
    }
}

data class DeviceState(
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

data class RestoreScreenState(
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

typealias TapeItem = Pair<OP1Resource.Tape, Boolean>

data class BackupInfo(
    val tapes: SnapshotStateList<TapeItem> =
        (0..<TAPES_COUNT).map { index ->
            OP1Resource.Tape(index = index, enabled = true) to false
        }.toMutableStateList(),
    val synths: Boolean = false,
    val synthsEnabled: Boolean = true,
    val drumkits: Boolean = false,
    val drumkitsEnabled: Boolean = true,
)

fun BackupInfo.isEmpty(): Boolean {
    return tapes.all { !it.first.enabled } && !synthsEnabled && !drumkitsEnabled
}

data class RestoreInfo(
    val tapes: SnapshotStateList<Pair<OP1Resource.Tape, Boolean>> =
        (0..<TAPES_COUNT).map { index -> OP1Resource.Tape(index) to false }.toList().toMutableStateList(),
    val synths: Boolean = false,
    val drumkits: Boolean = false,
)

sealed class OP1Resource {
    data class Tape(val index: Int, val enabled: Boolean = true) : OP1Resource()
    data class Synth(val index: Int) : OP1Resource()
    data class Drumkit(val index: Int) : OP1Resource()
}
