package org.jugregator.op1buddy.features.sync.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DeviceCopyingProgress(progress: Float, modifier: Modifier = Modifier) {
    val progressPercent = (progress * 100).toInt()
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Box {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.Center),
                    strokeWidth = 30.dp,
                    strokeCap = StrokeCap.Round,
                    progress = { progress },
                )
                Text(
                    text = "$progressPercent%",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.displaySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Now copying",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BackupScreenProgressPreview() {
    MaterialTheme {
        DeviceCopyingProgress(progress = 0.5f)
    }
}
