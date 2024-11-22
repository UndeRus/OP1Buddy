package org.jugregator.op1buddy.features.projects.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.data.project.Project
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun ProjectItem(
    project: Project,
    onClick: () -> Unit,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .weight(1f),
            text = project.title
        )
        var menuExpanded by remember { mutableStateOf(false) }
        IconButton({
            menuExpanded = true
        }) {
            Icon(painterResource(R.drawable.appbar_more), null)

            if (menuExpanded) {
                ProjectsContextMenu(menuExpanded, onExpandedChange = {
                    menuExpanded = it
                }, onSelected = { selectedAction ->
                    when(selectedAction) {
                        ProjectAction.EDIT -> onEditClicked()
                        ProjectAction.DELETE -> onDeleteClicked()
                    }
                })
            }
        }
    }
}

@Preview
@Composable
fun ProjectItemPreview() {
    val project = Project("2", "My long sommer jam at sunset", "")
    AppTheme {
        ProjectItem(project, {}, {}, {})
    }
}

enum class ProjectAction {
    EDIT,
    DELETE
}

@Composable
fun ProjectsContextMenu(expanded: Boolean, onExpandedChange: (Boolean) -> Unit, onSelected: (ProjectAction) -> Unit) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) },
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {

        DropdownMenuItem(onClick = {
            onExpandedChange(false)
            onSelected(ProjectAction.EDIT)
        }, text = {
            Text("Rename")
        })
        DropdownMenuItem(onClick = {
            onExpandedChange(false)
            onSelected(ProjectAction.DELETE)
        }, text = {
            Text("Delete")
        })
    }
}
