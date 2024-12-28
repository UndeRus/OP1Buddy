package org.jugregator.op1buddy.features.project.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun EmptySynthsView(modifier: Modifier = Modifier, onSyncClick: () -> Unit) {
    EmptyItemsView(modifier = modifier, picture = painterResource(R.drawable.empty_synths), onSyncClick = onSyncClick)
}

@Preview
@Composable
fun EmptySynthsViewPreview() {
    AppTheme {
        EmptySynthsView {}
    }
}
