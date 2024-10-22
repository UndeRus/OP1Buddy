package org.jugregator.op1buddy.features.projects.ui.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ProjectScreenTitle(modifier: Modifier = Modifier) {
    Text(
        text = "Companion for OP-1",
        modifier = modifier,
        style = MaterialTheme.typography.displayLarge.copy(color = MaterialTheme.colorScheme.tertiary),
        textAlign = TextAlign.Center
    )
}
