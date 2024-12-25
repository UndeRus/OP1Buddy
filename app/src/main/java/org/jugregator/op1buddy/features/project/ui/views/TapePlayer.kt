package org.jugregator.op1buddy.features.project.ui.views

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMapIndexed
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.features.project.ui.screens.ProjectResource
import org.jugregator.op1buddy.ui.theme.AppTheme
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.ceil
import kotlin.math.max
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
    isPlaying: Boolean = false,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onTrackToggle: (Int, Boolean) -> Unit,
    barColors: Array<Color> =
        arrayOf(
            Color(0xFFff3d3d),
            Color(0xFF1741b7),
            Color(0xFF2ae743),
        ),
    trackColor: Color = Color(0xFF1c1c1c),
    barColor: Color = Color(0xFFeaeaea),

    ) {
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


            maxRange = max(
                ceil(
                    maxRange / (SAMPLE_RATE * MIN_TAPE_LENGTH_SECONDS.toFloat())
                ).toLong() * SAMPLE_RATE * MIN_TAPE_LENGTH_SECONDS,
                SAMPLE_RATE * MIN_TAPE_LENGTH_SECONDS
            )
            if (maxRange == 0L) {
                maxRange = 10L
            }
        }
        minRange to maxRange
    }

    var canvasWidth by remember { mutableIntStateOf(-1) }

    val leftColumnWeight = .9f
    val rightColumnWeight = .15f
    val barHeight = 4.dp

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 28.dp), contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(R.drawable.player_player), contentDescription = null
            )

            val f: NumberFormat = DecimalFormat("00")
            val positionInSeconds = value / SAMPLE_RATE
            val frames = (value / (SAMPLE_RATE / FRAMES_PER_SECOND)) % FRAMES_PER_SECOND
            val minutes = positionInSeconds / MIN_TAPE_LENGTH_SECONDS
            val seconds = positionInSeconds % MIN_TAPE_LENGTH_SECONDS

            Text("$minutes:${f.format(seconds)}:${f.format(frames)}", style = MaterialTheme.typography.headlineSmall)

            val infiniteTransition = rememberInfiniteTransition(label = "infinite reel rotation")
            val leftAngle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -360f,
                animationSpec =
                infiniteRepeatable(
                    animation = tween(4000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "Left reel rotation"
            )
            val rightAngle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -360f,
                animationSpec =
                infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    // After each iteration of the animation (i.e. every 1000ms), the animation
                    // will
                    // start again from the [initialValue] defined above.
                    // This is the default [RepeatMode]. See [RepeatMode.Reverse] below for an
                    // alternative.
                    repeatMode = RepeatMode.Restart
                ),
                label = "Right reel rotation"
            )


            Image(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .graphicsLayer(rotationZ = if (isPlaying) leftAngle else 0f),
                painter = painterResource(R.drawable.reel), contentDescription = null
            )

            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .graphicsLayer(rotationZ = if (isPlaying) rightAngle else 0f),
                painter = painterResource(R.drawable.reel), contentDescription = null
            )
        }

        for (tapeIndex in 0 until tapeRanges.size) {

            Row(modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                val tape = tapeRanges[tapeIndex]
                var checked by remember { mutableStateOf(true) }

                val tapeRange = remember(tape, minRangeValue, maxRangeValue, canvasWidth) {
                    tape.map { range ->
                        val left = range.first.toFloat() / maxRangeValue * canvasWidth
                        val right = range.second.toFloat() / maxRangeValue * canvasWidth

                        (left to right)
                    }.toImmutableList()
                }

                val barColorsSize = barColors.size

                val rangeColor = remember(value, tape) {
                    tape.fastMapIndexed { index, _ ->
                        barColors[index % barColorsSize]
                    }.toImmutableList()
                }

                Canvas(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .background(barColor)
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
                        .weight(rightColumnWeight)
                ) {
                    MuteToggleView(
                        modifier = Modifier.offset(x = (-6).dp),
                        checked = checked, onCheckedChange = {
                            checked = it
                            onTrackToggle(tapeIndex, it)
                        })
                }
            }

        }
    }

    val thumbSize = 12.dp

    val sliderEnabled = true
    if (sliderEnabled) {
        Row {
            Slider(
                modifier = Modifier.weight(leftColumnWeight),
                value = value.toFloat(),
                onValueChange = { onValueChanged(it.roundToInt()) },
                onValueChangeFinished = onValueChangeFinished,
                valueRange = minRangeValue.toFloat()..(maxRangeValue - 1).toFloat(),

                track = { sliderState ->
                    val fraction by remember(sliderState) {
                        derivedStateOf {
                            (sliderState.value - sliderState.valueRange.start) /
                                (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                        }
                    }

                    val maxRangeInMinutes by remember {
                        derivedStateOf {
                            max(ceil(maxRangeValue / (SAMPLE_RATE * MIN_TAPE_LENGTH_SECONDS.toFloat())).toLong(), 1)
                        }
                    }

                    SliderTrack(
                        modifier = Modifier
                            .fillMaxWidth(),
                        fraction = fraction,
                        lengthInMinutes = maxRangeInMinutes,
                        trackColor = trackColor,
                        barColor = barColor,
                    )
                },
                thumb = {
                    Box(
                        Modifier
                            .padding(vertical = 2.5.dp)
                            .size(thumbSize)
                            .align(Alignment.CenterVertically)
                            .background(trackColor, CircleShape)
                    )
                }
            )
            //}
            Box(
                Modifier
                    .weight(rightColumnWeight)
            )
        }
    }

    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = onPlayClick) {
            Icon(painterResource(R.drawable.player_play), contentDescription = null)
        }
        IconButton(onClick = onPauseClick) {
            Icon(painterResource(R.drawable.player_pause), contentDescription = null)
        }
        IconButton(onClick = onStopClick) {
            Icon(painterResource(R.drawable.player_stop), contentDescription = null)
        }
    }

}

@Composable
fun SliderFilledTrack(modifier: Modifier = Modifier, fraction: Float, trackColor: Color = Color(0xFF1c1c1c)) {
    val filledBarHeight = 4.dp
    Box(
        modifier
            .background(color = trackColor)
            .height(filledBarHeight)
            .fillMaxWidth(fraction)
    )
}

@Composable
fun SliderTrackBackground(
    modifier: Modifier = Modifier,
    lengthInMinutes: Long = 6,
    marksColor: Color = Color(0xFF1c1c1c),
    barColor: Color = Color(0xFFeaeaea),
) {
    val tickWidth = 1.dp
    val trackHeight = 9.dp
    val barHeight = 4.dp
    val subminutesCount = 6
    val dotRadius = 1.dp

    Box(
        modifier
            .height(trackHeight)
            .fillMaxWidth()
            .drawBehind {

                drawRect(
                    barColor,
                    topLeft = Offset(0f, ((trackHeight / 2) - (barHeight / 2)).toPx()),
                    size = Size(size.width, barHeight.toPx())
                )

                for (step in 0..lengthInMinutes * subminutesCount) {
                    val x = 1f / (lengthInMinutes * subminutesCount) * step

                    if (step % subminutesCount == 0L) {

                        drawRect(
                            color = marksColor,
                            topLeft = Offset(
                                x * size.width
                                    - tickWidth.toPx() / 2, 0f
                            ),
                            size = Size(tickWidth.toPx(), trackHeight.toPx())
                        )
                    } else {

                        drawCircle(
                            color = marksColor,
                            radius = dotRadius.toPx(),
                            center = Offset(
                                x * size.width
                                    - (dotRadius / 2).toPx(), (trackHeight / 2).toPx()
                            )
                        )
                    }
                }

            }
    )
}

@Composable
fun SliderTrack(
    modifier: Modifier = Modifier,
    fraction: Float,
    lengthInMinutes: Long,
    trackColor: Color = Color(0xFF1c1c1c),
    barColor: Color = Color(0xFFeaeaea)
) {
    Box(modifier = modifier) {

        SliderTrackBackground(lengthInMinutes = lengthInMinutes, marksColor = trackColor, barColor = barColor)
        SliderFilledTrack(
            modifier = Modifier.align(Alignment.CenterStart),
            fraction = fraction,
            trackColor = trackColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SliderTrackBackgroundPreview(modifier: Modifier = Modifier) {
    AppTheme {
        SliderTrackBackground(lengthInMinutes = 1)
    }
}

@Preview(showBackground = true)
@Composable
private fun SliderTrackPreview() {
    AppTheme {
        SliderTrack(fraction = 0.5f, lengthInMinutes = 1)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, showBackground = true)
@Composable
fun MultiTrackSliderPreview() {
    AppTheme {
        Column(
            Modifier
                .padding(horizontal = 17.dp)
        ) {
            Box(modifier = Modifier.height(100.dp))
            var value by remember { mutableIntStateOf(762048) }
            var isPlaying by remember { mutableStateOf(false) }
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
                        //15748960L to 15875936L
                    ).toImmutableList(),

                    listOf(1L to 10L, 12L to 50L).toImmutableList(),
                    listOf(0L to 5L, 6L to 13L).toImmutableList(),
                    listOf(3L to 12L, 23L to 30L).toImmutableList(),
                    listOf(5L to 33L, 40L to 55L).toImmutableList(),
                ).toImmutableList(),
                isPlaying = isPlaying,
                onPlayClick = {
                    isPlaying = true
                },
                onPauseClick = {
                    isPlaying = false
                },
                onStopClick = {
                    isPlaying = false
                },
                onTrackToggle = { index, checked -> },
                trackColor = Color(0xFF1c1c1c)
            )
        }
    }
}

private const val SAMPLE_RATE: Long = 44100
private const val MIN_TAPE_LENGTH_SECONDS = 60
private const val FRAMES_PER_SECOND = 24
