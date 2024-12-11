package org.jugregator.op1buddy.features.project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.features.project.TapePlayerScreenViewModel
import org.jugregator.op1buddy.features.project.ui.views.MultiTrackPlayer
import org.koin.androidx.compose.koinViewModel

@Composable
fun TapePlayerScreen(modifier: Modifier = Modifier, viewModel: TapePlayerScreenViewModel = koinViewModel()) {
    Column(
        modifier
            .padding(horizontal = 17.dp)
    ) {
        var value by remember { mutableIntStateOf(0) }
        Text("Player")
        MultiTrackPlayer(
            value = value,
            onValueChanged = { value = it },
            onValueChangeFinished = {},
            tapeRanges = listOf(listOf(Pair(0, 100))),
            fromZero = true,
        )
    }
}
