package org.jugregator.op1buddy.features.projects.ui.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.Project
import org.jugregator.op1buddy.features.projects.ui.screens.ProjectNumber
import org.jugregator.op1buddy.features.projects.ui.screens.ProjectTitle
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun ProjectItem(project: Project, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(8.dp)) {
        ProjectNumber(id = project.id, onClick = onClick)
        ProjectTitle(
            title = project.title,
            onClick = onClick,
            modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
fun ProjectItemPreview() {
    val project = Project("2", "My long sommer jam at sunset", "")
    AppTheme {
        ProjectItem(project, {})
    }
}
