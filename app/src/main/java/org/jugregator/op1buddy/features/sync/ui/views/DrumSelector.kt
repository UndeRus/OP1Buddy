package org.jugregator.op1buddy.features.sync.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R

@Composable
fun DrumSelector(selected: Boolean, onSelected: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val animationDurationMillis = 1000

    val background: Color by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
        animationSpec = tween(animationDurationMillis),
        label = "backgroundAnimation",
    )

    val color: Color by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(animationDurationMillis),
        label = "foregroundAnimation",
    )

    Row(
        modifier = modifier
            .background(background, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelected.invoke(!selected) }
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.drum),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            colorFilter = ColorFilter.tint(color)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun DrumSelectorPreview() {
    var enabled by remember { mutableStateOf(false) }

    MaterialTheme {
        Column {
            DrumSelector(enabled, { enabled = !enabled })
        }
    }
}
