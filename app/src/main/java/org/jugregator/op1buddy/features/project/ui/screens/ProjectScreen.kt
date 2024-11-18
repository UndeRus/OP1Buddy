package org.jugregator.op1buddy.features.project.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.data.synth.SynthEngine
import org.jugregator.op1buddy.features.project.ProjectScreenViewModel
import org.jugregator.op1buddy.features.project.ui.views.ProjectBottomBar
import org.jugregator.op1buddy.features.project.ui.views.ProjectResourceItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectScreenViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
    onSyncClicked: (String) -> Unit,
    onDrumKitSelected: (String, Int) -> Unit,
) {
    val uiState by viewModel.state.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            ProjectAppBar(
                title = uiState.title,
                onBackClicked = onBackClicked,
                onMoreClicked = { viewModel.onMoreClicked() },
            )
        }, bottomBar = {
            ProjectBottomBar(
                selectedTab = uiState.tabOpened
            ) {
                if (it == ProjectTab.Sync) {
                    onSyncClicked(uiState.projectId)
                } else {
                    viewModel.selectTab(it)
                }
            }
        }
    ) { innerPadding ->
        val items by remember { derivedStateOf { uiState.items } }
        val lazyColumnState = rememberLazyListState()
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding)
        ) {
            Image(
                modifier = Modifier.align(Alignment.CenterEnd),
                painter = painterResource(R.drawable.background_right),
                contentDescription = null,
            )
            LazyColumn(state = lazyColumnState) {
                items(items, key = { it.filename }) {
                    ProjectResourceItem(it, {
                        if (it is ProjectResource.Drumkit) {
                            onDrumKitSelected(uiState.projectId, it.index)
                        }
                    })
                }
            }
        }

    }
    if (uiState.settingDialogOpened) {
        ProjectSettingsDialog(
            projectTitle = uiState.title,
            onDismissRequest = {
                viewModel.onSettingsDialogDismiss()
            }, onConfirmation = { projectTitle ->
                viewModel.onSettingsDialogConfirm(projectTitle)
            })
    }
}

sealed class ProjectResource(val index: Int, val filename: String) {
    class Synth(index: Int, filename: String, val name: String, val engine: SynthEngine) :
        ProjectResource(index, filename)

    class Drumkit(index: Int, filename: String, val name: String) : ProjectResource(index, filename)
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
        ProjectScreen(
            onBackClicked = {},
            onSyncClicked = {},
            onDrumKitSelected = { projectId, drumkitId -> },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectAppBar(modifier: Modifier = Modifier, title: String, onBackClicked: () -> Unit, onMoreClicked: () -> Unit) {
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
                    painter = painterResource(R.drawable.appbar_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = onMoreClicked
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(R.drawable.appbar_more),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    )
}

@Preview
@Composable
fun ProjectAppBarPreview() {
    MaterialTheme {
        ProjectAppBar(title = "Project name", onBackClicked = {}, onMoreClicked = {})
    }
}

@Composable
fun ProjectSettingsDialog(
    projectTitle: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Project name",
                    modifier = Modifier.padding(16.dp),
                )

                var projectTitleValue by remember { mutableStateOf(projectTitle) }
                TextField(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    value = projectTitleValue,
                    onValueChange = { projectTitleValue = it },
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation(projectTitleValue) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProjectSettingsDialogPreview() {
    MaterialTheme {
        ProjectSettingsDialog("Title", {}, {})
    }
}