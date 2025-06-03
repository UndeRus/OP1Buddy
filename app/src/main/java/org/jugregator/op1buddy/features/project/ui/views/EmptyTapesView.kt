package org.jugregator.op1buddy.features.project.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.ui.icons.Icons
import org.jugregator.op1buddy.ui.icons.empty.Tapes
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun EmptyTapesView(modifier: Modifier = Modifier, onSyncClick: () -> Unit) {
    EmptyItemsView(modifier = modifier, picture = Icons.Empty.Tapes, onSyncClick = onSyncClick)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyTapesViewPreview() {
    AppTheme {
        EmptyTapesView(modifier = Modifier) {}
    }
}
