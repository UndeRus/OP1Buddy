package org.jugregator.op1buddy.features.project.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.data.LCE
import org.jugregator.op1buddy.features.project.SynthListScreenViewModel
import org.jugregator.op1buddy.features.project.ui.views.EmptySynthsView
import org.jugregator.op1buddy.features.project.ui.views.SynthResourceItem
import org.jugregator.op1buddy.features.project.ui.views.SynthsLoadingView
import org.koin.androidx.compose.koinViewModel

@Composable
fun SynthListScreen(
    modifier: Modifier = Modifier,
    viewModel: SynthListScreenViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    onSyncClick: () -> Unit,
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
        viewModel.loadSynths()
        onStopOrDispose {
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.align(Alignment.CenterEnd),
            painter = painterResource(R.drawable.background_right),
            contentDescription = null,
        )

        when(val data = uiState.data) {
            LCE.Loading -> {
                SynthsLoadingView()
            }
            LCE.Error -> {
                //TODO: show error
            }
            is LCE.Content -> {
                val items: List<ProjectResource.Synth> by remember {
                    derivedStateOf {
                        data.data.mapIndexed { index, synthInfo ->
                            ProjectResource.Synth(
                                index = index,
                                engine = synthInfo.synthEngine,
                                filename = synthInfo.filename,
                                name = synthInfo.name
                            )
                        }
                    }
                }
                if (items.isEmpty()) {
                    EmptySynthsView(modifier = Modifier.padding(horizontal = 16.dp)) { onSyncClick() }
                } else {
                    val lazyColumnState = rememberLazyListState()
                    LazyColumn(state = lazyColumnState) {
                        items(items, key = { it.filename }) {
                            SynthResourceItem(synth = it)
                        }
                    }
                }
            }
        }
    }
}
