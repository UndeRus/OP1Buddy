package org.jugregator.op1buddy.features.sync.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun DeviceCopyingProgress(title: String, progress: Float, modifier: Modifier = Modifier) {
    val progressPercent = (progress * 100).toInt()
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Box {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(234.dp)
                        .align(Alignment.Center),
                    strokeWidth = 12.dp,
                    trackColor = progressColor,
                    strokeCap = StrokeCap.Square,
                    gapSize = 0.dp,
                    progress = { progress },
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(168.dp)
                        .background(progressColor, shape = CircleShape)
                )

                Text(
                    text = "$progressPercent%",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 64.sp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Wait until 100% load",
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 28.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DeviceCopyingPreview() {
    AppTheme {
        DeviceCopyingProgress("NOW DOWNLOADING", progress = 0.5f)
    }
}

private val progressColor = Color(0xFFEAEAEA)
