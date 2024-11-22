package org.jugregator.op1buddy.features.projects.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.data.project.Project
import org.jugregator.op1buddy.features.project.ui.views.ProjectDeleteDialog
import org.jugregator.op1buddy.features.project.ui.views.ProjectSettingsDialog
import org.jugregator.op1buddy.features.projects.ProjectsScreenViewModel
import org.jugregator.op1buddy.features.projects.ui.views.ProjectItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectsScreenViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
    onProjectClicked: (Project) -> Unit,
    onNewProjectClicked: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(modifier = modifier,
        topBar = {
            ProjectsAppBar(onBackClicked = onBackClicked, onSearchClicked = { })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNewProjectClicked() },
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Icon(painterResource(R.drawable.fab_plus), "Create new project.")
            }
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            val projects by remember { derivedStateOf { state.projects } }
            Image(
                modifier = Modifier.align(Alignment.BottomCenter),
                painter = painterResource(R.drawable.background_bottom),
                contentDescription = null
            )

            var editDialogOpened by remember { mutableStateOf(false) }
            var deleteDialogOpened by remember { mutableStateOf(false) }
            LazyColumn {
                itemsIndexed(projects, key = { index, project -> project.id }) { index, project ->
                    ProjectItem(
                        modifier = Modifier.fillParentMaxWidth(),
                        project = project, onClick = {
                            onProjectClicked(project)
                        }, onDeleteClicked = {
                            deleteDialogOpened = true
                        }, onEditClicked = {
                            editDialogOpened = true
                        })
                    if (editDialogOpened) {
                        ProjectSettingsDialog(
                            projectTitle = project.title,
                            onDismissRequest = {
                                editDialogOpened = false
                            },
                            onConfirmation = { title ->
                                viewModel.editProjectConfirmed(project, title)
                            }
                        )
                    }
                    if (deleteDialogOpened) {
                        ProjectDeleteDialog(
                            projectTitle = project.title,
                            onDismissRequest = {
                                deleteDialogOpened = false
                            },
                            onConfirmation = {
                                viewModel.removeProjectConfirmed(project)
                            }
                        )
                    }
                    if (index < projects.size) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }

        }
    }

}

/*
@Preview
@Composable
fun ProjectsScreenPreview() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        AppTheme {
            ProjectsScreen(onBackClicked = {}, onProjectClicked = {}, onNewProjectClicked = {})
        }
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsAppBar(modifier: Modifier = Modifier, onBackClicked: () -> Unit, onSearchClicked: () -> Unit) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier.padding(start = 35.dp),
                text = "Projects",
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
                onClick = onSearchClicked
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(R.drawable.appbar_action_search),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    )
}

@Preview
@Composable
fun ProjectsAppBarPreview() {
    MaterialTheme {
        ProjectsAppBar(onBackClicked = {}, onSearchClicked = {})
    }
}
