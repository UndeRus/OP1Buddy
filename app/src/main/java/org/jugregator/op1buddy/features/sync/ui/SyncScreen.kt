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
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.platform.LocalContext
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
        3
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
            Tab(
                selected = pagerState.currentPage == 2,
                onClick = { scope.launch { pagerState.animateScrollToPage(2) } },
                text = {
                    Text(
                        text = "Export"
                    )
                },
                icon = {
                    Icon(Icons.Default.Share, contentDescription = null)
                }
            )
        })

        val context = LocalContext.current

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
                    val uiState by viewModel.stateFlow.collectAsState()
                    val isLoading by remember {
                        derivedStateOf { uiState.nowCopying }
                    }

                    val backupInfo by remember {
                        derivedStateOf {
                            uiState.backupInfo
                        }
                    }

                    val deviceConnected by remember {
                        derivedStateOf { uiState.connected }
                    }

                    val progress by remember {
                        derivedStateOf { uiState.progress }
                    }

                    BackupScreen(
                        isLoading,
                        progress,
                        onBackupClick = { viewModel.backupDevice() },
                        backupInfo = backupInfo,
                        connectedState = deviceConnected,
                        onBackupSelectionChanged = {
                            viewModel.updateBackupInfo(it)
                        })
                }

                1 -> RestoreScreen()
                2 -> {
                    val uiState by viewModel.stateFlow.collectAsState()
                    val isLoading by remember {
                        derivedStateOf { uiState.nowCopying }
                    }
                    ExportScreen(isCopying = isLoading, onBackupDirSelected = { uri ->
                        viewModel.onBackupDirSelected(context, uri)
                    })
                }
            }
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