package org.jugregator.op1buddy.features.project.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.features.project.SynthListScreenViewModel
import org.jugregator.op1buddy.features.project.ui.views.SynthResourceItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SynthListScreen(
    modifier: Modifier = Modifier,
    viewModel: SynthListScreenViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    //TODO: dirty hack remove
    val errorEvent by viewModel.errorFinish.collectAsState(-1)

    LaunchedEffect(errorEvent) {
        if (errorEvent == 1) {
            onBackPressed()
        }
    }

    val items: List<ProjectResource.Synth> by remember {
        derivedStateOf {
            uiState.synths.mapIndexed { index, synthInfo ->
                ProjectResource.Synth(
                    index = index,
                    engine = synthInfo.synthEngine,
                    filename = synthInfo.filename,
                    name = synthInfo.name
                )
            }
        }
    }
    val lazyColumnState = rememberLazyListState()
    Box(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier.align(Alignment.CenterEnd),
            painter = painterResource(R.drawable.background_right),
            contentDescription = null,
        )
        LazyColumn(state = lazyColumnState) {
            items(items, key = { it.filename }) {
                SynthResourceItem(synth = it)
            }
        }
    }
}
