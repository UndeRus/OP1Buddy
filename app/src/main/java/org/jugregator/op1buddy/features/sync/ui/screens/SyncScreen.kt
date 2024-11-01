package org.jugregator.op1buddy.features.sync.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Create
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SyncScreen(modifier: Modifier = Modifier, viewModel: OP1SyncViewModel = koinViewModel()) {

    val context = LocalContext.current
    LaunchedEffect(viewModel.backupStateFlow.collectAsState().value.backupInfo) {
        viewModel.init(context)
    }


    val pagerState = rememberPagerState(pageCount = {
        3
    })

    val restoreState by viewModel.restoreStateFlow.collectAsState()
    val backupState by viewModel.backupStateFlow.collectAsState()

    val isBusy by remember {
        derivedStateOf {
            restoreState.nowCopying || backupState.nowCopying
        }
    }

    val isBusyAlpha by animateFloatAsState(
        if (isBusy) {
            0.7f
        } else {
            1f
        }, label = "disabledTabBarAlpha"
    )

    val scope = rememberCoroutineScope()
    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = pagerState.currentPage, tabs = {
                Tab(
                    enabled = !isBusy,
                    selected = pagerState.currentPage == 0,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },

                    text = { Text(text = "Backup", modifier = Modifier.padding(10.dp)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Create,
                            modifier = Modifier.alpha(isBusyAlpha),
                            contentDescription = null
                        )
                    }
                )
                Tab(
                    enabled = !isBusy,
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    text = {
                        Text(
                            text = "Restore"
                        )
                    },
                    icon = {
                        Icon(Icons.AutoMirrored.Filled.Send, modifier = Modifier.alpha(isBusyAlpha), contentDescription = null)
                    }
                )
                Tab(
                    enabled = !isBusy,
                    selected = pagerState.currentPage == 2,
                    onClick = { scope.launch { pagerState.animateScrollToPage(2) } },
                    text = {
                        Text(
                            text = "Export"
                        )
                    },
                    icon = {
                        Icon(Icons.Default.Share, modifier = Modifier.alpha(isBusyAlpha), contentDescription = null)
                    }
                )
            })

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
                    val uiState by viewModel.restoreStateFlow.collectAsState()
                    val isLoading by remember {
                        derivedStateOf { uiState.nowCopying }
                    }
                    ExportScreen(state = uiState, isCopying = isLoading, onBackupDirSelected = { uri ->
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
