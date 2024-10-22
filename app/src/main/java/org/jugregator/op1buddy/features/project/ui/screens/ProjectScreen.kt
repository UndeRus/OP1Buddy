package org.jugregator.op1buddy.features.project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.Project
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.features.project.ProjectScreenViewModel
import org.jugregator.op1buddy.features.projects.ui.screens.ProjectNumber
import org.jugregator.op1buddy.features.projects.ui.screens.ProjectTitle
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext
import org.koin.core.KoinApplication

@Composable
fun ProjectScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectScreenViewModel = koinViewModel(),
    onSyncClicked: (String) -> Unit
) {
    Column(modifier = modifier) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(getItems()) {
                ProjectResourceItem(ProjectResource.Synth, {})
            }
        }
        Row {
            // Synths
            IconButton(onClick = {}) {
                Icon(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(R.drawable.synth),
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    contentDescription = null
                )
            }

            // Drums
            IconButton(onClick = {}) {
                Icon(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(R.drawable.drum),
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    contentDescription = null
                )
            }

            // Tapes
            IconButton(onClick = {}) {
                Icon(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(R.drawable.tapes),
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    contentDescription = null
                )
            }

            // Sync
            IconButton(onClick = { onSyncClicked(viewModel.route.projectId) }) {
                Icon(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(R.drawable.tapes),
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    contentDescription = null
                )
            }
        }
    }
}

enum class ProjectResource {
    Synth,
    Drumkit,
    Tape
}

private fun getItems(): List<Int> {
    return listOf(
        1, 2, 3, 4, 5, 6, 7, 8
    )
}

@Composable
fun ProjectResourceItem(type: ProjectResource, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(8.dp)) {
        ProjectNumber(id = "0", onClick = onClick)
        ProjectTitle(
            title = "Synth 0",
            onClick = onClick,
            modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
fun ProjectScreenPreview() {
    MaterialTheme {
        ProjectScreen {}
    }
}