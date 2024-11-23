package org.jugregator.op1buddy.features.sync.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R

@Composable
fun SyncBottomBar(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    selectedTab: SyncTab,
    onTabSelected: (SyncTab) -> Unit
) {
    Row(modifier = modifier.clip(RectangleShape)) {
        Box(modifier = Modifier.width(10.dp))
        Box(modifier = Modifier.weight(0.2f))
        SyncBottomBarButton(
            title = "Backup",
            selected = selectedTab == SyncTab.Backup,
            modifier = Modifier
                .height(118.dp)
                .width(76.dp),
            icon = R.drawable.bb_backup,
            enabled = enabled,
        ) {
            onTabSelected(SyncTab.Backup)
        }


        Box(modifier = Modifier.width(10.dp))
        SyncBottomBarButton(
            title = "Restore",
            selected = selectedTab == SyncTab.Restore,
            modifier = Modifier
                .height(118.dp)
                .width(76.dp),
            icon = R.drawable.bb_recover,
            enabled = enabled,
        ) {
            onTabSelected(SyncTab.Restore)
        }


        Box(modifier = Modifier.width(10.dp))
        SyncBottomBarButton(
            title = "Export",
            selected = selectedTab == SyncTab.Export,
            modifier = Modifier
                .height(118.dp)
                .width(76.dp),
            icon = R.drawable.bb_export,
            enabled = enabled,
        ) {
            onTabSelected(SyncTab.Export)
        }

        Box(modifier = Modifier.weight(0.2f))
        Box(modifier = Modifier.width(10.dp))
    }
}

enum class SyncTab {
    Backup,
    Restore,
    Export
}

@Preview
@Composable
fun SyncBottomBarPreview() {
    MaterialTheme {
        var selectedTab by remember { mutableStateOf(SyncTab.Backup) }
        SyncBottomBar(selectedTab = selectedTab, enabled = true) {
            selectedTab = it
        }
    }
}
