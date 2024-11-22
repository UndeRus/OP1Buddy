package org.jugregator.op1buddy.features.sync.ui.views

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jugregator.op1buddy.features.sync.OP1ConnectionState
import org.jugregator.op1buddy.features.sync.ui.screens.DeviceConnectingScreen
import org.jugregator.op1buddy.features.sync.ui.screens.DeviceNotConnectedScreen

@Composable
fun UsbConnectionWrapper(
    connectedState: OP1ConnectionState,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    when (connectedState) {
        OP1ConnectionState.Disconnected -> {
            DeviceNotConnectedScreen(modifier = modifier.fillMaxHeight())
        }

        OP1ConnectionState.Connecting -> {
            DeviceConnectingScreen(modifier = modifier)
        }

        else -> {
            content()
        }
    }
}
