package org.jugregator.op1buddy.features.project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.features.project.ProjectScreenViewModel
import org.jugregator.op1buddy.features.project.ui.views.ProjectBottomBar
import org.jugregator.op1buddy.features.project.ui.views.ProjectResourceItem
import org.jugregator.op1buddy.features.sync.OP1Resource
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectScreenViewModel = koinViewModel(),
    onSyncClicked: (String) -> Unit,
    onDrumKitSelected: (String, Int) -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    Column(modifier = modifier) {
        if (uiState.titleEditEnabled) {
            Row {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                )
                IconButton(onClick = { viewModel.saveProject() }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                }
            }
        } else {
            Row {
                Text(modifier = Modifier
                    .clickable {
                        viewModel.onEditTitleClicked()
                    }, text = uiState.title.ifEmpty { "..." })
            }
        }

        val items by remember { derivedStateOf { uiState.items } }

        val lazyColumnState = rememberLazyListState()
        LazyColumn(modifier = Modifier.weight(1f), state = lazyColumnState) {
            items(items, key = { it.filename }) {
                ProjectResourceItem(it, {
                    if (it is ProjectResource.Drumkit) {
                        onDrumKitSelected(uiState.projectId, it.index)
                    }
                })
            }
        }


        ProjectBottomBar(selectedTab = uiState.tabOpened) {
            if (it == ProjectTab.Sync) {
                onSyncClicked(uiState.projectId)
            } else {
                viewModel.selectTab(it)
            }
        }
    }
}

sealed class ProjectResource(val index: Int, val filename: String) {
    class Synth(index: Int, filename: String) : ProjectResource(index, filename)
    class Drumkit(index: Int, filename: String) : ProjectResource(index, filename)
    class Tape(index: Int, filename: String) : ProjectResource(index, filename)
}

enum class ProjectTab {
    Synth,
    Drumkit,
    Tape,
    Sync,
}

@Preview
@Composable
fun ProjectScreenPreview() {
    MaterialTheme {
        ProjectScreen(onSyncClicked = {}, onDrumKitSelected = { projectId, drumkitId -> })
    }
}