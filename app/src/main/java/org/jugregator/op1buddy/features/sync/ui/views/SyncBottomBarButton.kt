package org.jugregator.op1buddy.features.sync.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R

@Composable
fun SyncBottomBarButton(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes icon: Int,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {

    val animatedOffset by animateDpAsState(if (selected) 2.dp else 23.dp, label = "BottomBarButton shift")
    val animatedColorBg by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.surface,
        label = "BottomBarButton animated background",
    )
    val animatedColorBorder by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.surfaceTint else
            MaterialTheme.colorScheme.outlineVariant,
        label = "BottomBarButton animated border",
    )

    val animatedTint by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.outline,
        label = "BottomBarButton animated tint",
    )

    Column(modifier = modifier
        .offset { IntOffset(x = 0.dp.roundToPx(), y = animatedOffset.roundToPx()) }
        .border(2.dp, animatedColorBorder, RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
        .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
        .background(animatedColorBg)
        .clickable(enabled = enabled, onClick = {
            onClick()
        }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 14.dp)) {
                Icon(
                    modifier = Modifier
                        .size(48.dp),
                    painter = painterResource(icon),
                    tint = animatedTint,
                    contentDescription = null
                )
                Text(text = title, color = animatedTint, style = MaterialTheme.typography.labelLarge)
            }
        }

    }
}

@Preview
@Composable
fun SyncBottomBarButtonPreview() {
    MaterialTheme {
        var selected by remember { mutableStateOf(false) }

        SyncBottomBarButton(
            modifier = Modifier.size(86.dp, 118.dp),
            title = "Backup",
            icon = R.drawable.bb_backup, selected = selected, enabled = true, onClick = {
                selected = !selected
            }
        )
    }
}

