package org.jugregator.op1buddy.features.sync.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.features.sync.BackupInfo
import org.jugregator.op1buddy.features.sync.BackupScreenState
import org.jugregator.op1buddy.features.sync.OP1ConnectionState
import org.jugregator.op1buddy.features.sync.RestoreScreenState
import org.jugregator.op1buddy.features.sync.ui.views.DeviceCopyingProgress
import org.jugregator.op1buddy.features.sync.ui.views.DrumSelector
import org.jugregator.op1buddy.features.sync.ui.views.RestoreReadyForCopy
import org.jugregator.op1buddy.features.sync.ui.views.SynthSelector
import org.jugregator.op1buddy.features.sync.ui.views.TapeSelector
import org.jugregator.op1buddy.features.sync.ui.views.UsbConnectionWrapper

@Composable
fun RestoreScreenTodo(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "In construction...",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun RestoreScreen(
    state: RestoreScreenState,
    onRestoreClick: () -> Unit,
    onRestoreSelectionChanged: (BackupInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    UsbConnectionWrapper(connectedState = state.connected, content = {
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
        var backupInfo = BackupInfo()
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