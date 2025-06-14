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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.jugregator.op1buddy.data.LCE
import org.jugregator.op1buddy.data.drumkit.DrumkitType
import org.jugregator.op1buddy.features.project.DrumkitListScreenViewModel
import org.jugregator.op1buddy.features.project.ui.views.DrumkitResourceItem
import org.jugregator.op1buddy.features.project.ui.views.EmptyDrumkitsView
import org.jugregator.op1buddy.ui.icons.Icons
import org.jugregator.op1buddy.ui.icons.background.BackgroundRight
import org.koin.androidx.compose.koinViewModel

@Composable
fun DrumkitListScreen(
    modifier: Modifier = Modifier,
    viewModel: DrumkitListScreenViewModel = koinViewModel(),
    onDrumKitSelected: (String, Int) -> Unit,
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
        viewModel.loadDrumkits()
        onStopOrDispose {

        }
    }


    val lazyColumnState = rememberLazyListState()
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.align(Alignment.CenterEnd),
            imageVector = Icons.Background.BackgroundRight,
            contentDescription = null,
        )


        when(val data = uiState.data) {
            LCE.Loading -> {
                //DrumkitLoadingView()
                //TODO: show loading
            }
            LCE.Error -> {
                //TODO: show error
            }
            is LCE.Content -> {
                val items: List<ProjectResource.Drumkit> by remember {
                    derivedStateOf {
                        data.data.mapIndexed { index, drumkitInfo ->
                            ProjectResource.Drumkit(
                                index = index,
                                filename = drumkitInfo.filename,
                                type = drumkitInfo.drumType,
                                name = drumkitInfo.name
                            )
                        }
                    }
                }

                if (items.isEmpty()) {
                    EmptyDrumkitsView(modifier = Modifier.padding(horizontal = 16.dp),) {
                        onSyncClick()
                    }
                } else {
                    LazyColumn(state = lazyColumnState) {
                        items(items, key = { it.filename }) {
                            DrumkitResourceItem(drumkit = it, onClick = {
                                viewModel.onDrumKitSelected(it.index) { projectId, drumkitIndex ->
                                    if (it.type == DrumkitType.Sample) {
                                        onDrumKitSelected(projectId, drumkitIndex)
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}
