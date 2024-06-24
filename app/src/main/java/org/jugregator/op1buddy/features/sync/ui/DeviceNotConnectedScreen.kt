package org.jugregator.op1buddy.features.sync.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.ui.theme.AppTheme

@Composable
fun DeviceNotConnectedScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Image(painter = painterResource(id = R.drawable.op1_connect_faq), contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
fun DeviceNotConnectedScreenPreview() {
    AppTheme {
        DeviceNotConnectedScreen()
    }
}