package org.jugregator.op1buddy.features.sync.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R

@Composable
fun DrumSelector(
    selected: Boolean,
    onSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val animationDurationMillis = 1000

    val background: Color by animateColorAsState(
        targetValue = if (enabled) {
            if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onPrimary
            }
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.38f)
        },
        animationSpec = tween(animationDurationMillis),
        label = "backgroundAnimation",
    )

    val color: Color by animateColorAsState(
        targetValue = if (enabled) {
            if (selected) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.primary
            }
        } else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.38f
        ),
        animationSpec = tween(animationDurationMillis),
        label = "foregroundAnimation",
    )

    OutlinedCard(onClick = { onSelected(!selected)}, enabled = enabled, modifier = modifier) {
        Box(modifier = Modifier.size(78.dp).background(background), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.drum_selector),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DrumSelectorPreview() {
    var enabled by remember { mutableStateOf(false) }

    MaterialTheme {
        Column {
            DrumSelector(enabled, { enabled = !enabled })
            DrumSelector(false, { }, enabled = false)
        }
    }
}
