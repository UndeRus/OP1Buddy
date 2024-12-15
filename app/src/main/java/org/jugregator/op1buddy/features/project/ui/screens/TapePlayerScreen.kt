package org.jugregator.op1buddy.features.project.ui.screens

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.collections.immutable.toImmutableList
import org.jugregator.op1buddy.features.project.TapePlayerScreenViewModel
import org.jugregator.op1buddy.features.project.ui.views.MultiTrackPlayer
import org.koin.androidx.compose.koinViewModel

@Composable
fun TapePlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: TapePlayerScreenViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
) {
    Column(
        modifier
            .padding(horizontal = 17.dp)
    ) {
        val uiState by viewModel.uiState.collectAsState()

        //TODO: dirty hack remove
        val errorEvent by viewModel.errorFinish.collectAsState(-1)

        LaunchedEffect(errorEvent) {
            if (errorEvent == 1) {
                onBackPressed()
            }
        }
        val lifecycleOwner = LocalLifecycleOwner.current
        LifecycleStartEffect(viewModel, lifecycleOwner = lifecycleOwner) {
            viewModel.loadTapes()
            onStopOrDispose {
                viewModel.stop()
            }
        }


        val tapeRanges by remember {
            derivedStateOf {
                uiState.tapes.map {
                    it.second.regions.regions.toImmutableList()
                }
            }
        }

        var value by remember { mutableIntStateOf(0) }

        val animatedSliderValue by animateIntAsState(
            targetValue = uiState.position.toInt(),
            label = "Slider Value"
        )
        Text("Player")
        MultiTrackPlayer(
            value = animatedSliderValue,
            onValueChanged = { newValue ->
                value = newValue
                viewModel.seekToSample(newValue.toLong(), false)
            },
            onValueChangeFinished = {
                viewModel.seekToSample(value.toLong(), true)
            },
            tapeRanges = tapeRanges.toImmutableList(),
            fromZero = true,
            onPlayClick = {
                viewModel.play()
            },
            onPauseClick = {
                viewModel.pause()
            },
            onStopClick = {
                viewModel.stop()
            },
            onTrackToggle = { index, checked ->
                viewModel.toggleTrack(index, checked)
            }
        )
    }
}
