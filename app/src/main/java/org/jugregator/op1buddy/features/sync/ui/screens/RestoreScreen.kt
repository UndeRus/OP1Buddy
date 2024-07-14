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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.features.sync.BackupInfo
import org.jugregator.op1buddy.features.sync.RestoreScreenState
import org.jugregator.op1buddy.features.sync.TAPES_COUNT
import org.jugregator.op1buddy.features.sync.ui.views.DeviceCopyingProgress
import org.jugregator.op1buddy.features.sync.ui.views.DrumSelector
import org.jugregator.op1buddy.features.sync.ui.views.SynthSelector
import org.jugregator.op1buddy.features.sync.ui.views.TapeRow
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
    modifier: Modifier = Modifier) {
    UsbConnectionWrapper(connectedState = state.connected, content = {
        if (state.nowCopying) {
            DeviceCopyingProgress(progress = state.progress)
        } else {
            RestoreReadyForCopy(state, onRestoreSelectionChanged, onRestoreClick, modifier)
        }
    })
}

@Composable fun RestoreReadyForCopy(
    state: RestoreScreenState,
    onRestoreSelectionChanged: (BackupInfo) -> Unit,
    onRestoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Choose what to restore",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (tapeIndex in 0..1) {
                TapeSelector(
                    index = tapeIndex,
                    selected = state.backupInfo.tapes[tapeIndex].second, { selected ->
                        val newBackupInfo = state.backupInfo.copy()
                        newBackupInfo.tapes[tapeIndex] = state.backupInfo.tapes[tapeIndex].copy(second = selected)
                        onRestoreSelectionChanged(state.backupInfo.copy(tapes = newBackupInfo.tapes))
                    })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (tapeIndex in 2..3) {
                TapeSelector(
                    index = tapeIndex,
                    selected = state.backupInfo.tapes[tapeIndex].second, { selected ->
                        val newBackupInfo = state.backupInfo.copy()
                        newBackupInfo.tapes[tapeIndex] = state.backupInfo.tapes[tapeIndex].copy(second = selected)
                        onRestoreSelectionChanged(state.backupInfo.copy(tapes = newBackupInfo.tapes))
                    })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterHorizontally),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            SynthSelector(selected = state.backupInfo.synths, onSelected = { checked ->
                onRestoreSelectionChanged(state.backupInfo.copy(synths = checked))
            })

            DrumSelector(selected = state.backupInfo.drumkits, onSelected = { checked ->
                onRestoreSelectionChanged(state.backupInfo.copy(drumkits = checked))
            })
        }

        Spacer(modifier = Modifier.weight(1.0f))

    }
}
