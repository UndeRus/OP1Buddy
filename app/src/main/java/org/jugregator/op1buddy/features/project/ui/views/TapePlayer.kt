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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jugregator.op1buddy.features.project.ui.screens.ProjectResource
import org.jugregator.op1buddy.ui.theme.AppTheme
import kotlin.math.roundToInt

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiTrackPlayer(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChanged: (Int) -> Unit,
    onValueChangeFinished: (() -> Unit)?,
    tapeRanges: ImmutableList<ImmutableList<Pair<Long, Long>>>,
    fromZero: Boolean = true,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onTrackToggle: (Int, Boolean) -> Unit,
) {
    // TODO: make single flatten

    val (minRangeValue, maxRangeValue) = remember(tapeRanges, fromZero) {

        var minRange = if (fromZero) 0L else Long.MAX_VALUE
        var maxRange = Long.MIN_VALUE

        if (tapeRanges.size == 0) {
            minRange = 0L
            maxRange = 10L
        } else {
            for (singleTape in tapeRanges) {
                for (range in singleTape) {
                    if (!fromZero && range.first < minRange) {
                        minRange = range.first
                    }

                    if (range.second > maxRange) {
                        maxRange = range.second
                    }
                }
            }
            if (maxRange == 0L) {
                maxRange = 10L
            }
        }
        minRange to maxRange
    }

    var canvasWidth by remember { mutableIntStateOf(-1) }


    val leftColumnWeight = .9f
    val rightColumnWeight = .15f
    val barHeight = 30.dp

    Column(modifier = modifier) {

        for (tapeIndex in 0 until tapeRanges.size) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                val tape = tapeRanges[tapeIndex]
                var checked by remember { mutableStateOf(true) }

                val tapeRange = remember(tape, minRangeValue, maxRangeValue, canvasWidth) {
                    tape.map { range ->
                        val left = range.first.toFloat() / maxRangeValue * canvasWidth
                        val right = range.second.toFloat() / maxRangeValue * canvasWidth

                        (left to right)
                    }.toImmutableList()
                }

                val rangeColor = remember(value, checked, tape) {
                    tape.map { range ->
                        val color = if (!checked) {
                            Color.Gray
                        } else if (value in range.first..range.second) {
                            Color.Green
                        } else {
                            Color.Black
                        }
                        color
                    }.toImmutableList()
                }

                Canvas(
                    modifier = Modifier
                        .weight(leftColumnWeight)
                        .height(barHeight)
                        .onSizeChanged {
                            if (canvasWidth == -1) {
                                canvasWidth = it.width
                            }
                        }
                ) {
                    // Call first time to calculate width
                    if (canvasWidth == -1) {
                        return@Canvas
                    }

                    val barHeightPx = barHeight.toPx()

                    for (singleRangeIndex in 0 until tapeRange.size) {
                        val singleRange = tapeRange[singleRangeIndex]
                        val color = rangeColor[singleRangeIndex]
                        val (left, right) = singleRange
                        drawRect(
                            color,
                            topLeft = Offset(x = left, y = 0f),
                            size = Size(width = right - left, height = barHeightPx)
                        )
                    }
                }

                Box(
                    Modifier
                        .padding(start = 10.dp)
                        .weight(rightColumnWeight)
                ) {
                    Checkbox(checked, {
                        checked = it
                        onTrackToggle(tapeIndex, it)
                    })
                }
            }

        }
    }

    Row {
        Slider(
            modifier = Modifier.weight(leftColumnWeight),
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
                .weight(rightColumnWeight)
                .padding(end = 10.dp)
        )
    }

    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        IconButton({}) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
        }
        IconButton(onClick = onPlayClick) {
            Icon(Icons.Filled.PlayArrow, contentDescription = null)
        }
        IconButton(onClick = onPauseClick) {
            Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
        }
        IconButton(onClick = onStopClick) {
            Icon(Icons.Filled.Home, contentDescription = null)
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
                onValueChangeFinished = {
                    println("Slider value $value")
                },
                tapeRanges = listOf(

                    listOf(
                        0L to 84672L,
                        84672L to 169344L,
                        169344L to 338688L,
                        338688L to 423360L,
                        592704L to 1016064L,
                        1185408L to 1524096L,
                        15748960L to 15875936L
                    ).toImmutableList(),

                    listOf(1L to 10L, 12L to 50L).toImmutableList(),
                    listOf(0L to 5L, 6L to 13L).toImmutableList(),
                    listOf(3L to 12L, 23L to 30L).toImmutableList(),
                    listOf(5L to 33L, 40L to 55L).toImmutableList(),
                ).toImmutableList(),
                onPlayClick = {},
                onStopClick = {},
                onPauseClick = {},
                onTrackToggle = { index, checked -> }
            )
        }
    }
}
