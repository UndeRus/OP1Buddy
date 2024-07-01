package org.jugregator.op1buddy.features.sync.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.features.sync.BackupInfo
import org.jugregator.op1buddy.features.sync.BackupScreenState
import org.jugregator.op1buddy.features.sync.TAPES_COUNT

@Composable
fun BackupReadyForCopy(
    state: BackupScreenState,
    onBackupSelectionChanged: (BackupInfo) -> Unit,
    onBackupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Choose what to backup",
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
                        onBackupSelectionChanged(state.backupInfo.copy(tapes = newBackupInfo.tapes))
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
                        onBackupSelectionChanged(state.backupInfo.copy(tapes = newBackupInfo.tapes))
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
                onBackupSelectionChanged(state.backupInfo.copy(synths = checked))
            })

            DrumSelector(selected = state.backupInfo.drumkits, onSelected = { checked ->
                onBackupSelectionChanged(state.backupInfo.copy(drumkits = checked))
            })
        }

        Spacer(modifier = Modifier.weight(1.0f))

        Button(
            onClick = onBackupClick,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            enabled = !state.nowCopying
        ) {
            Text(text = "Backup", style = MaterialTheme.typography.displayLarge)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BackupReadyForCopyPreview() {
    MaterialTheme {
        BackupReadyForCopy(state = BackupScreenState(), onBackupSelectionChanged = {}, onBackupClick = { /*TODO*/ })
    }
}
