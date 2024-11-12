package org.jugregator.op1buddy.features.drumkit.ui.screen

//import androidx.media3.common.util.UnstableApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.features.drumkit.DrumKitScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DrumKitScreen(modifier: Modifier = Modifier, viewModel: DrumKitScreenViewModel = koinViewModel()) {
    val names by remember {
        mutableStateOf(
            listOf(
                "Kick",
                "Kick Alt",
                "Snare",
                "Snare Alt",
                "Rimshot",
                "Clap",
                "Tambourine",
                "08",
                "Closed Hi-Hat",
                "09",
                "Open Hi-Hat",
                "10",
                "11",
                "Ride",
                "12",
                "Crash",
                "13",
                "?",
                "14",
                "Bass 1",
                "Bass 2",
                "Bass 3",
                "Bass 4",
                "Bass 5",
            )
        )
    }

    Column(modifier = modifier.fillMaxHeight()) {
        val state by viewModel.uiState.collectAsState()
        val mediaSize by remember {
            derivedStateOf {
                state.drumCount
            }
        }
        for (i in 0..<(mediaSize / 3)) {
            Row(modifier = Modifier.weight(0.125f)) {
                for (j in 0..<3) {
                    Button(modifier = Modifier.fillMaxHeight().weight(0.3f), onClick = {
                        viewModel.play(i * 3 + j)
                    }) {
                        Text(names[i * 3 + j])
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DrumKitScreenPreview() {
    MaterialTheme {
        DrumKitScreen()
    }
}