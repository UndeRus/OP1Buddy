package org.jugregator.op1buddy.features.sync.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import org.jugregator.op1buddy.features.sync.TAPES_COUNT
import org.jugregator.op1buddy.features.sync.ui.views.BackupProgress
import org.jugregator.op1buddy.features.sync.ui.views.BackupReadyForCopy
import org.jugregator.op1buddy.features.sync.ui.views.TapeRow
import org.jugregator.op1buddy.features.sync.ui.views.UsbConnectionWrapper

@Composable
fun BackupScreen(
    state: BackupScreenState,
    onBackupClick: () -> Unit,
    onBackupSelectionChanged: (BackupInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    UsbConnectionWrapper(connectedState = state.connected, content = {
        if (state.nowCopying) {
            BackupProgress(state.progress, modifier)
        } else {
            BackupReadyForCopy(state, onBackupSelectionChanged, onBackupClick, modifier)
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