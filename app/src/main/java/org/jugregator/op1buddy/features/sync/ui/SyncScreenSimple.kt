package org.jugregator.op1buddy.features.sync.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.jugregator.op1buddy.features.sync.DeviceState

@Composable
fun SyncScreenSimple(
    stateProducer: () -> DeviceState,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val fs by remember {
            derivedStateOf { stateProducer().fs }
        }
        if (fs != null) {
            Text(text = "Device connected")
        } else {
            Text(text = "Device disconnected")
        }

        val error by remember {
            derivedStateOf { stateProducer().error }
        }

        if (stateProducer().error != null) {
            Text(text = "Failed to init device $error")
        }

        val fsState by remember {
            derivedStateOf { stateProducer().fsState }
        }

        if (fsState != null && fs != null) {
            Text(text = "Found: ${fsState?.tracks?.size} tracks, ${fsState?.albums?.size} albums, ${fsState?.synths?.size} synths, ${fsState?.drums?.size} drums")
            val nowCopying by remember {
                derivedStateOf { stateProducer().nowCopying }
            }
            Button(onClick = onBackupClick, enabled = !nowCopying) {
                Text(text = "Try to copy from OP-1")
            }

            Text(text = "Percents of file was read ${stateProducer().bytesWasReadPercent}%")
            Button(onClick = onRestoreClick, enabled = !nowCopying) {
                Text(text = "Try to copy to OP-1")
            }
            Text(text = "Percents of file was wrote ${stateProducer().bytesWasWritePercent}%")

        }
    }
}
