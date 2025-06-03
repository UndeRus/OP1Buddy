package org.jugregator.op1buddy.features.drumkit.ui.screen

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.features.drumkit.DrumKitScreenViewModel
import org.jugregator.op1buddy.features.drumkit.ui.views.FourPianoKeys
import org.jugregator.op1buddy.features.drumkit.ui.views.ThreePianoKeys
import org.jugregator.op1buddy.ui.icons.Icons
import org.jugregator.op1buddy.ui.icons.appbar.Back
import org.koin.androidx.compose.koinViewModel

@Composable
fun DrumKitScreen(
    modifier: Modifier = Modifier,
    viewModel: DrumKitScreenViewModel = koinViewModel(),
    onBackClicked: () -> Unit
) {
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

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        DrumkitAppBar(
            title = uiState.title,
            onBackClicked = onBackClicked,
        )
    }) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(modifier = modifier
            .padding(innerPadding)
            .verticalScroll(scrollState)) {
            FourPianoKeys(
                fTitle = names[0],
                onFClick = { viewModel.play(0) },
                gTitle = names[2],
                onGClick = { viewModel.play(2) },
                aTitle = names[4],
                onAClick = { viewModel.play(4) },
                bTitle = names[6],
                onBClick = { viewModel.play(6) },
                fSharpTitle = names[1],
                onFSharpClick = { viewModel.play(1) },
                gSharpTitle = names[3],
                onGSharpClick = { viewModel.play(3) },
                aSharpTitle = names[5],
                onASharpClick = { viewModel.play(5) },
            )
            ThreePianoKeys(
                cTitle = names[7],
                onCClick = { viewModel.play(7) },
                cSharpTitle = names[9],
                onCSharpClick = { viewModel.play(9) },
                dTitle = names[11],
                onDClick = { viewModel.play(11) },
                dSharpTitle = names[8],
                onDSharpClick = { viewModel.play(8) },
                eTitle = names[10],
                onEClick = { viewModel.play(10) },
            )

            FourPianoKeys(
                fTitle = names[12],
                onFClick = { viewModel.play(12) },
                gTitle = names[14],
                onGClick = { viewModel.play(14) },
                aTitle = names[16],
                onAClick = { viewModel.play(16) },
                bTitle = names[18],
                onBClick = { viewModel.play(18) },
                fSharpTitle = names[13],
                onFSharpClick = { viewModel.play(13) },
                gSharpTitle = names[15],
                onGSharpClick = { viewModel.play(15) },
                aSharpTitle = names[17],
                onASharpClick = { viewModel.play(17) },
            )


            ThreePianoKeys(
                cTitle = names[19],
                onCClick = { viewModel.play(19) },
                cSharpTitle = names[21],
                onCSharpClick = { viewModel.play(21) },
                dTitle = names[23],
                onDClick = { viewModel.play(23) },
                dSharpTitle = names[20],
                onDSharpClick = { viewModel.play(20) },
                eTitle = names[22],
                onEClick = { viewModel.play(22) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrumkitAppBar(modifier: Modifier = Modifier, title: String, onBackClicked: () -> Unit) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .basicMarquee(),
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 8.dp),
                onClick = onBackClicked
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icons.AppBar.Back,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
    )
}
