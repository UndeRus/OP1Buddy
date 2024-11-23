package org.jugregator.op1buddy.features.sync.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jugregator.op1buddy.features.sync.BackupInfo
import org.jugregator.op1buddy.features.sync.BackupScreenState

@Composable
fun BackupReadyForCopy(
    state: BackupScreenState,
    onBackupSelectionChanged: (BackupInfo) -> Unit,
    onBackupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Choose current item to download from OP-1",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(60.dp)
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

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(60.dp)
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

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 60.dp, alignment = Alignment.CenterHorizontally),
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

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onBackupClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
            ),
            enabled = !state.nowCopying
        ) {
            Text(
                text = "Download",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.Black)
            )
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
