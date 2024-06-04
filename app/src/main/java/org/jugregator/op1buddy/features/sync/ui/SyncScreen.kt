package org.jugregator.op1buddy.features.sync.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.jugregator.op1buddy.features.sync.BackupInfo
import org.jugregator.op1buddy.features.sync.OP1ConnectionState
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.features.sync.TAPES_COUNT
import org.jugregator.op1buddy.ui.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SyncScreen(modifier: Modifier = Modifier, viewModel: OP1SyncViewModel = viewModel()) {
    val pagerState = rememberPagerState(pageCount = {
        2
    })

    val scope = rememberCoroutineScope()
    Column(modifier = modifier) {
        TabRow(selectedTabIndex = pagerState.currentPage, tabs = {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = { scope.launch { pagerState.animateScrollToPage(0) } },

                text = { Text(text = "Backup", modifier = Modifier.padding(10.dp)) },
                icon = {
                    Icon(imageVector = Icons.Default.Create, contentDescription = null)
                }
            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                text = {
                    Text(
                        text = "Restore"
                    )
                },
                icon = {
                    Icon(Icons.Default.Send, contentDescription = null)
                }
            )
        })

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
                    val uiState by viewModel.stateFlow.collectAsState()
                    val isLoading by remember {
                        derivedStateOf { uiState.nowCopying }
                    }
                    var backupInfo by remember {
                        mutableStateOf(BackupInfo())
                    }

                    val deviceConnected by remember {
                        derivedStateOf { uiState.connected }
                    }

                    BackupScreen(
                        isLoading,
                        onBackupClick = { viewModel.backupDevice() },
                        backupInfo = backupInfo,
                        connectedState = deviceConnected,
                        onBackupSelectionChanged = {
                            backupInfo = it
                            viewModel.updateBackupInfo(backupInfo)
                        })
                }

                1 -> RestoreScreen()
            }
        }
    }
}

@Composable
fun BackupScreen(
    isLoading: Boolean,
    connectedState: OP1ConnectionState,
    onBackupClick: () -> Unit,
    backupInfo: BackupInfo,
    onBackupSelectionChanged: (BackupInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    when (connectedState) {
        OP1ConnectionState.Disconnected -> {
            DeviceNotConnectedScreen()
            return
        }
        OP1ConnectionState.Connecting -> {
            DeviceConnectingScreen()
            return
        }
        else -> {}
    }

    Column(modifier = modifier) {
        Text(
            text = "Choose what to backup",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp)
        )
        for (tapeIndex in 0..<TAPES_COUNT) {
            TapeRow(tapeIndex, backupInfo.tapes[tapeIndex].second, { selected ->
                val newBackupInfo = backupInfo.copy()
                newBackupInfo.tapes[tapeIndex] = backupInfo.tapes[tapeIndex].copy(second = selected)
                onBackupSelectionChanged(backupInfo.copy(tapes = newBackupInfo.tapes))
            })
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = backupInfo.synths, onCheckedChange = { checked ->
                onBackupSelectionChanged(backupInfo.copy(synths = checked))
            })
            Text(text = "Synths")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = backupInfo.drumkits, onCheckedChange = { checked ->
                onBackupSelectionChanged(backupInfo.copy(drumkits = checked))
            })
            Text(text = "Drumkits")
        }
        Spacer(modifier = Modifier.weight(1.0f)) // fill height with spacer

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Button(
            onClick = onBackupClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && connectedState == OP1ConnectionState.Connected
        ) {
            Text(text = "Backup")
        }
    }
}

@Composable
fun TapeRow(numberOfTape: Int, checked: Boolean, onCheckedChanged: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChanged)
        Text(text = "Tape ${numberOfTape + 1}")
    }
}

@Composable
fun RestoreScreen() {
    Column {

        Text(
            text = "Choose what to restore",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp)
        )
        for (tapeIndex in 1..TAPES_COUNT) {
            TapeRow(tapeIndex, false, {})
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = false, onCheckedChange = {})
            Text(text = "Synths")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = false, onCheckedChange = {})
            Text(text = "Drumkits")
        }
        Spacer(modifier = Modifier.weight(1.0f)) // fill height with spacer

        val isLoading by remember {
            mutableStateOf(true)
        }

        if (isLoading) {
            Text(
                text = "Don't close your app it can broke your filesystem",
                style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.error)
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading) {
            Text(text = "Restore")
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun SyncScreenPreview() {
    AppTheme {
        SyncScreen()
    }
}