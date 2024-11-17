package org.jugregator.op1buddy.features.project.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.features.project.ui.screens.ProjectTab

@Composable
fun ProjectBottomBar(modifier: Modifier = Modifier, selectedTab: ProjectTab, onTabSelected: (ProjectTab) -> Unit) {
    Row(modifier = modifier.clip(RectangleShape)) {
        Box(modifier = Modifier.width(10.dp))
        BottomBarButton(
            selected = selectedTab == ProjectTab.Synth,
            modifier = Modifier
                .height(100.dp)
                .weight(0.25f), icon = R.drawable.bb_synth
        ) {
            onTabSelected(ProjectTab.Synth)
        }

        Box(modifier = Modifier.width(10.dp))
        BottomBarButton(
            selected = selectedTab == ProjectTab.Drumkit,
            modifier = Modifier
                .height(100.dp)
                .weight(0.25f), icon = R.drawable.bb_drumkit
        ) {
            onTabSelected(ProjectTab.Drumkit)
        }

        Box(modifier = Modifier.width(10.dp))
        BottomBarButton(
            selected = selectedTab == ProjectTab.Tape,
            modifier = Modifier
                .height(100.dp)
                .weight(0.25f), icon = R.drawable.bb_tape
        ) {
            onTabSelected(ProjectTab.Tape)
        }

        Box(modifier = Modifier.width(10.dp))
        BottomBarButton(
            selected = false,
            modifier = Modifier
                .height(100.dp)
                .weight(0.25f), icon = R.drawable.bb_sync
        ) {
            onTabSelected(ProjectTab.Sync)
        }
        Box(modifier = Modifier.width(10.dp))
    }
}

/*
fun Modifier.selected(selected: Boolean): Modifier =
    Modifier.let { if (selected) it.background(Color.Red) else it }
*/

fun Modifier.selected(condition: Boolean): Modifier {
    return if (condition) {
        this.border(BorderStroke(1.dp, Color.Red))
    } else {
        this
    }
}

@Composable
fun BottomBarButton(modifier: Modifier = Modifier, @DrawableRes icon: Int, selected: Boolean, onClick: () -> Unit) {

    val animatedOffset by animateDpAsState(if (selected) 2.dp else 20.dp, label = "BottomBarButton shift")
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
        .clickable {
            onClick()
        }) {
        Box(
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.Center),
                painter = painterResource(icon),
                tint = animatedTint,
                contentDescription = null
            )
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 2.dp)
    }
}

@Preview
@Composable
fun BottomBarButtonPreview() {
    MaterialTheme {
        var selected by remember { mutableStateOf(false) }

        BottomBarButton(
            modifier = Modifier.size(86.dp, 100.dp),
            icon = R.drawable.bb_sync, selected = selected, onClick = {
                selected = !selected
            }
        )
    }
}

@Preview
@Composable
fun ProjectBottomBarPreview() {
    MaterialTheme {
        ProjectBottomBar(selectedTab = ProjectTab.Synth) { }
    }
}
