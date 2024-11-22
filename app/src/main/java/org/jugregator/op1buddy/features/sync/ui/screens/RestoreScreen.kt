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
import org.jugregator.op1buddy.features.sync.OP1ConnectionState
import org.jugregator.op1buddy.features.sync.RestoreScreenState
import org.jugregator.op1buddy.features.sync.ui.views.DeviceCopyingProgress
import org.jugregator.op1buddy.features.sync.ui.views.RestoreReadyForCopy
import org.jugregator.op1buddy.features.sync.ui.views.UsbConnectionWrapper

@Composable
fun RestoreScreen(
    state: RestoreScreenState,
    onRestoreClick: () -> Unit,
    onRestoreSelectionChanged: (BackupInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    UsbConnectionWrapper(modifier = modifier,
        connectedState = state.connected, content = {
            if (state.nowCopying) {
                WakeLock()
                DeviceCopyingProgress(progress = state.progress)
            } else {
                RestoreReadyForCopy(
                    state = state,
                    onRestoreSelectionChanged = onRestoreSelectionChanged,
                    onRestoreClick = onRestoreClick,
                    modifier = modifier
                )
            }
        })
}

@Preview
@Composable
fun RestoreScreenPreview() {
    MaterialTheme {
        val backupInfo = BackupInfo()
        var (tape1, selected) = backupInfo.tapes[0]
        //tape1.first =
        tape1 = tape1.copy(enabled = false)
        backupInfo.tapes[0] = (tape1 to selected)
        val state = RestoreScreenState(connected = OP1ConnectionState.Connected, backupInfo = backupInfo)
        var restoreState by remember {
            mutableStateOf(state)
        }


        RestoreScreen(state = restoreState, onRestoreClick = { /*TODO*/ }, onRestoreSelectionChanged = {
            restoreState = restoreState.copy(backupInfo = it)
        })
    }
}
