package org.jugregator.op1buddy.features.sync.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
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
import org.jugregator.op1buddy.features.sync.TAPES_COUNT

@Composable
fun BackupScreen(
    state: BackupScreenState,

//    isLoading: Boolean,
//    progress: Float,
//    connectedState: OP1ConnectionState,
    onBackupClick: () -> Unit,
//    backupInfo: BackupInfo,
    onBackupSelectionChanged: (BackupInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    UsbConnectionWrapper(connectedState = state.connected, content = {
        Column(modifier = modifier) {
            Text(
                text = "Choose what to backup",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(10.dp)
            )
            for (tapeIndex in 0..<TAPES_COUNT) {
                TapeRow(tapeIndex, state.backupInfo.tapes[tapeIndex].second, { selected ->
                    val newBackupInfo = state.backupInfo.copy()
                    newBackupInfo.tapes[tapeIndex] = state.backupInfo.tapes[tapeIndex].copy(second = selected)
                    onBackupSelectionChanged(state.backupInfo.copy(tapes = newBackupInfo.tapes))
                })
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.backupInfo.synths, onCheckedChange = { checked ->
                    onBackupSelectionChanged(state.backupInfo.copy(synths = checked))
                })
                Text(text = "Synths")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.backupInfo.drumkits, onCheckedChange = { checked ->
                    onBackupSelectionChanged(state.backupInfo.copy(drumkits = checked))
                })
                Text(text = "Drumkits")
            }
            Spacer(modifier = Modifier.weight(1.0f)) // fill height with spacer

            if (state.nowCopying) {
                val progressPercent = (state.progress * 100).toInt()
                Text(text = "Now copying... $progressPercent%", modifier = Modifier.align(Alignment.CenterHorizontally))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = state.progress
                )
            }
            Button(
                onClick = onBackupClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.nowCopying
            ) {
                Text(text = "Backup")
            }
        }
    })

}

@Composable
fun TapeRow(
    numberOfTape: Int,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChanged)
        Text(text = "Tape ${numberOfTape + 1}")
    }
}

@Composable
fun UsbConnectionWrapper(
    connectedState: OP1ConnectionState,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    when (connectedState) {
        OP1ConnectionState.Disconnected -> {
            DeviceNotConnectedScreen(modifier = modifier)
        }

        OP1ConnectionState.Connecting -> {
            DeviceConnectingScreen(modifier = modifier)
        }

        else -> {
            content()
        }
    }
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