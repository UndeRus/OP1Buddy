package org.jugregator.op1buddy.features.sync.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.features.sync.BackupInfo
import org.jugregator.op1buddy.features.sync.BackupScreenState
import org.jugregator.op1buddy.features.sync.ui.views.BackupReadyForCopy
import org.jugregator.op1buddy.features.sync.ui.views.DeviceCopyingProgress
import org.jugregator.op1buddy.features.sync.ui.views.UsbConnectionWrapper

@Composable
fun BackupScreen(
    state: BackupScreenState,
    onBackupClick: () -> Unit,
    onBackupSelectionChanged: (BackupInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    UsbConnectionWrapper(modifier = modifier, connectedState = state.connected, content = {
        if (state.nowCopying) {
            WakeLock()
            DeviceCopyingProgress(state.progress, modifier)
        } else {
            BackupReadyForCopy(
                state = state,
                onBackupSelectionChanged = onBackupSelectionChanged,
                onBackupClick = onBackupClick,
                modifier = modifier
            )
        }
    })

}

@Preview
@Composable
fun BackupScreenPreview() {
    MaterialTheme {
        var backupState by remember {
            mutableStateOf(BackupScreenState())
        }

        BackupScreen(
            state = backupState,
            onBackupClick = { /*TODO*/ },
            onBackupSelectionChanged = {
                backupState = backupState.copy(backupInfo = it)
            }
        )
    }
}