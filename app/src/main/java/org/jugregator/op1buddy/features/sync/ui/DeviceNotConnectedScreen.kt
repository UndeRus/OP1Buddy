package org.jugregator.op1buddy.features.sync.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun DeviceNotConnectedScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Device is not connected", style = MaterialTheme.typography.displayLarge)
        Text(text = "Join it in DISK mode with USB cable and accept permission", style = MaterialTheme.typography.displayLarge)
    }
}

@Preview
@Composable
fun DeviceNotConnectedScreenPreview() {
    AppTheme {
        DeviceNotConnectedScreen()
    }
}