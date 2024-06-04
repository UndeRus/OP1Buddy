package org.jugregator.op1buddy.features.sync.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun DeviceConnectingScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Device is now connecting...", style = MaterialTheme.typography.titleLarge)
        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Preview
@Composable
fun DeviceConnectingScreenPreview() {
    AppTheme {
        DeviceConnectingScreen()
    }
}