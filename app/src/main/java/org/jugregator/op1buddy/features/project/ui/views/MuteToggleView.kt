package org.jugregator.op1buddy.features.project.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.ui.icons.Icons
import org.jugregator.op1buddy.ui.icons.player.TrackLoud
import org.jugregator.op1buddy.ui.icons.player.TrackMuted

@Composable
fun MuteToggleView(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val painter = if (!checked) {
        Icons.Player.TrackMuted
    } else {
        Icons.Player.TrackLoud
    }

    Image(
        modifier = modifier.clickable {
            onCheckedChange(!checked)
        },
        painter = key(painter) { rememberVectorPainter(painter) },
        contentDescription = null,
    )
}

@Preview
@Composable
fun MuteToggleViewPreview() {
    var checked by remember { mutableStateOf(false) }
    MuteToggleView(checked, { checked = it })
}
