package org.jugregator.op1buddy.features.project.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import org.jugregator.op1buddy.features.project.ui.screens.ProjectResource
import org.jugregator.op1buddy.ui.theme.AppTheme
import kotlin.math.roundToInt

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun TapePlayer(modifier: Modifier = Modifier, onPlayClick: () -> Unit, onStopClick: () -> Unit) {
    Row(modifier = modifier) {
        Button({ onPlayClick() }) {
            Text("Play")
        }
        Button({ onStopClick() }) {
            Text("Stop")
        }
    }
}

@Composable
fun SeparateTape(modifier: Modifier = Modifier, tape: ProjectResource.Tape, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
        ) {
            TapeIcon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
            )
        }
        Text(
            modifier = modifier
                .padding(12.dp)
                .weight(1f),
            text = "Tape ${tape.index + 1}"
        )
    }
}

@Suppress("NonSkippableComposable")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiTrackPlayer(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChanged: (Int) -> Unit,
    onValueChangeFinished: (() -> Unit)?,
    tapeRanges: List<List<Pair<Long, Long>>>,
    fromZero: Boolean = true,
) {
    val minRangeValue by remember {
        derivedStateOf {
            if (fromZero) {
                0L
            } else {
                tapeRanges.flatten().minByOrNull { it.first }?.first ?: 0L
            }
        }
    }
    val maxRangeValue by remember { derivedStateOf { tapeRanges.flatten().maxByOrNull { it.second }?.second ?: 0L } }
    Column(modifier = modifier) {

        for (range in tapeRanges) {

            val barHeight = 30.dp
            Row(verticalAlignment = Alignment.CenterVertically) {
                var checked by remember { mutableStateOf(false) }
                Canvas(
                    modifier = Modifier
                        .weight(.9f)
                        .height(barHeight)
                ) {
                    val canvasWidth = size.width



                    for ((rangeStart, rangeEnd) in range) {
                        val color = if (!checked) {
                            Color.Gray
                        } else if (value in rangeStart..rangeEnd) {
                            Color.Green
                        } else {
                            Color.Black
                        }

                        val left = rangeStart.toFloat() / maxRangeValue * canvasWidth
                        val right = rangeEnd.toFloat() / maxRangeValue * canvasWidth
                        drawRect(
                            color,
                            topLeft = Offset(x = left, y = 0f),
                            size = Size(width = right - left, height = barHeight.toPx())
                        )
                    }
                }

                Box(
                    Modifier
                        .padding(start = 10.dp)
                        .weight(.1f)
                ) {
                    Checkbox(checked, { checked = it })
                }
            }

        }
    }


    Row {
        Slider(
            modifier = Modifier.weight(0.9f),
            steps = maxRangeValue.toInt() - 1,
            value = value.toFloat(),
            onValueChange = { onValueChanged(it.roundToInt()) },
            onValueChangeFinished = onValueChangeFinished,
            valueRange = minRangeValue.toFloat()..(maxRangeValue - 1).toFloat(),
            track = { sliderState ->
                val fraction by remember {
                    derivedStateOf {
                        (sliderState.value - sliderState.valueRange.start) /
                        (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                    }
                }

                Box(Modifier.fillMaxWidth()) {
                    Box(
                        Modifier
                            .fillMaxWidth(fraction)
                            .align(Alignment.CenterStart)
                            .height(6.dp)
                            .padding(end = 16.dp)
                            .background(Color.Red, CircleShape)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth(1f - fraction)
                            .align(Alignment.CenterEnd)
                            .height(1.dp)
                            .padding(start = 16.dp)
                            .background(Color.Blue, CircleShape)
                    )
                }
            }
        )
        Box(
            Modifier
                .weight(.1f)
                .padding(end = 10.dp)
        )
    }

    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        IconButton({}) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
        }
        IconButton({}) {
            Icon(Icons.Filled.PlayArrow, contentDescription = null)
        }
        IconButton({}) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }

}

@Preview
@Composable
fun MultiTrackSliderPreview() {
    AppTheme {
        Column(
            Modifier
                .padding(horizontal = 17.dp)
        ) {
            Box(modifier = Modifier.height(100.dp))
            var value by remember { mutableIntStateOf(0) }
            MultiTrackPlayer(
                value = value,
                onValueChanged = { value = it },
                tapeRanges = listOf(
                    listOf(1L to 10L, 12L to 50L),
                    listOf(0L to 5L, 6L to 13L),
                    listOf(3L to 12L, 23L to 30L),
                    listOf(5L to 33L, 40L to 55L),
                ),
                onValueChangeFinished = {
                    println("Slider value $value")
                }
            )
        }
    }
}
