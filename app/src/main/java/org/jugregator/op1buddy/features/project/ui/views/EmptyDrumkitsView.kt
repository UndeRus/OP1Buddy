package org.jugregator.op1buddy.features.project.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.ui.icons.Icons
import org.jugregator.op1buddy.ui.icons.empty.Drums
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun EmptyDrumkitsView(modifier: Modifier = Modifier, onSyncClick: () -> Unit) {
    EmptyItemsView(modifier = modifier, picture = Icons.Empty.Drums, onSyncClick = onSyncClick)
}

@Preview
@Composable
fun EmptyDrumkitsViewPreview() {
    AppTheme {
        EmptyDrumkitsView {  }
    }
}
