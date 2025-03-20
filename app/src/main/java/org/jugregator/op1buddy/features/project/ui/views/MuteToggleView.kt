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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.R

@Composable
fun MuteToggleView(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val id = if (!checked) {
        R.drawable.track_muted
    } else {
        R.drawable.track_loud
    }
    Image(
        modifier = modifier.clickable {
            onCheckedChange(!checked)
        },
        painter = key(id) { painterResource(id) }, contentDescription = null
    )
}

@Preview
@Composable
fun MuteToggleViewPreview() {
    var checked by remember { mutableStateOf(false) }
    MuteToggleView(checked, { checked = it })
}
