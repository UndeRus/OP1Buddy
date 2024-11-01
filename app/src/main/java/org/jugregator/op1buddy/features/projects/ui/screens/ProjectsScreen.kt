package org.jugregator.op1buddy.features.projects.ui.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jugregator.op1buddy.Project
import org.jugregator.op1buddy.features.projects.ProjectsScreenViewModel
import org.jugregator.op1buddy.features.projects.ui.views.ProjectItem
import org.jugregator.op1buddy.features.projects.ui.views.ProjectScreenTitle
import org.jugregator.op1buddy.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectsScreenViewModel = koinViewModel(),
    onProjectClicked: (Project) -> Unit,
    onNewProjectClicked: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(modifier = modifier, floatingActionButton = {
        FloatingActionButton(
            onClick = { onNewProjectClicked() },
        ) {
            Icon(Icons.Filled.Add, "Create new project.")
        }
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ProjectScreenTitle(modifier = Modifier.fillMaxWidth())
            val projects by remember { derivedStateOf { state.projects } }
            LazyColumn(modifier = modifier.weight(1f)) {
                items(projects, key = { project -> project.id }) { project ->
                    ProjectItem(project = project, onClick = {
                        onProjectClicked(project)
                    })
                }
            }

        }
    }

}

@Preview
@Composable
fun ProjectsScreenPreview() {
    AppTheme {
        ProjectsScreen(onProjectClicked = {}, onNewProjectClicked = {})
    }
}

@Composable
fun ProjectNumber(id: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        shape = CircleShape,
        onClick = onClick,
        modifier = modifier.size(80.dp)
    ) {
        Text(
            text = "O",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 25.sp)
        )
    }

}

@Composable
fun ProjectTitle(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(onClick = onClick, modifier = modifier.height(80.dp)) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 25.sp)
        )
    }
}
