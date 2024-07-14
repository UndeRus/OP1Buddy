package org.jugregator.op1buddy.features.sync.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R

@Composable
fun TapeSelector(
    index: Int,
    selected: Boolean,
    onSelected: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val animationDurationMillis = 1000
    val background: Color by animateColorAsState(
        targetValue = if (enabled) {
            if (selected) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.surface
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
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        } else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.38f
        ),
        animationSpec = tween(animationDurationMillis),
        label = "foregroundAnimation",
    )

    val rotation: Float by animateFloatAsState(
        targetValue = if (selected) 180f else 0f,
        animationSpec = tween(durationMillis = animationDurationMillis),
        label = "tapeRotation",
    )

    Row(
        modifier = modifier
            .background(background, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = { onSelected.invoke(!selected) })
            .padding(16.dp)
    ) {
        Text(
            text = (index + 1).toString(),
            style = MaterialTheme.typography.displayLarge,
            color = color,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.tape2),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier
                .size(100.dp)
                .rotate(rotation)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun TapeSelectorPreview() {
    var enabled by remember { mutableStateOf(false) }

    MaterialTheme {
        Column {
            TapeSelector(index = 0, selected = enabled, { enabled = !enabled })
            TapeSelector(index = 1, selected = false, enabled = false, onSelected = { })

        }
    }
}
