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
import org.jugregator.op1buddy.features.sync.OP1ConnectionState
import org.jugregator.op1buddy.features.sync.TAPES_COUNT

@Composable
fun BackupScreen(
    isLoading: Boolean,
    progress: Float,
    connectedState: OP1ConnectionState,
    onBackupClick: () -> Unit,
    backupInfo: BackupInfo,
    onBackupSelectionChanged: (BackupInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    when (connectedState) {
        OP1ConnectionState.Disconnected -> {
            DeviceNotConnectedScreen()
            return
        }

        OP1ConnectionState.Connecting -> {
            DeviceConnectingScreen()
            return
        }

        else -> {}
    }

    Column(modifier = modifier) {
        Text(
            text = "Choose what to backup",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp)
        )
        for (tapeIndex in 0..<TAPES_COUNT) {
            TapeRow(tapeIndex, backupInfo.tapes[tapeIndex].second, { selected ->
                val newBackupInfo = backupInfo.copy()
                newBackupInfo.tapes[tapeIndex] = backupInfo.tapes[tapeIndex].copy(second = selected)
                onBackupSelectionChanged(backupInfo.copy(tapes = newBackupInfo.tapes))
            })
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = backupInfo.synths, onCheckedChange = { checked ->
                onBackupSelectionChanged(backupInfo.copy(synths = checked))
            })
            Text(text = "Synths")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = backupInfo.drumkits, onCheckedChange = { checked ->
                onBackupSelectionChanged(backupInfo.copy(drumkits = checked))
            })
            Text(text = "Drumkits")
        }
        Spacer(modifier = Modifier.weight(1.0f)) // fill height with spacer

        if (isLoading) {
            val progressPercent = (progress * 100).toInt()
            Text(text = "Now copying... $progressPercent%", modifier = Modifier.align(Alignment.CenterHorizontally))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = progress
            )
        }
        Button(
            onClick = onBackupClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && connectedState == OP1ConnectionState.Connected
        ) {
            Text(text = "Backup")
        }
    }
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

@Preview
@Composable
fun BackupScreenPreview() {
    MaterialTheme {
        var backupInfo by remember {
            mutableStateOf(BackupInfo())
        }

        BackupScreen(
            isLoading = false,
            progress = 0.5f,
            connectedState = OP1ConnectionState.Connected,
            onBackupClick = { /*TODO*/ },
            backupInfo = backupInfo,
            onBackupSelectionChanged = {
                backupInfo = it
            }
        )
    }
}