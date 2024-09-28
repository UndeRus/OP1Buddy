package org.jugregator.op1buddy.features.sync.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.ui.theme.AppTheme

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
                    val uiState by viewModel.backupStateFlow.collectAsState()
                    BackupScreen(
                        state = uiState,
                        onBackupClick = { viewModel.backupDevice() },
                        onBackupSelectionChanged = {
                            viewModel.updateBackupInfo(it)
                        })
                }

                1 -> {
                    val uiState by viewModel.restoreStateFlow.collectAsState()
                    RestoreScreen(
                        state = uiState,
                        onRestoreClick = { viewModel.restoreDevice() },
                        onRestoreSelectionChanged = {
                            viewModel.updateRestoreInfo(it)
                        })
                }

                2 -> {
                    val uiState by viewModel.backupStateFlow.collectAsState()
                    val isLoading by remember {
                        derivedStateOf { uiState.nowCopying }
                    }
                    ExportScreen(isCopying = isLoading, onBackupDirSelected = { uri ->
                        viewModel.onBackupExportDirSelected(context, uri)
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