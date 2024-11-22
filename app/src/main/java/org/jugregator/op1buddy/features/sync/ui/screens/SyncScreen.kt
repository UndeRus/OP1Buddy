package org.jugregator.op1buddy.features.sync.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.features.sync.ui.views.SyncBottomBar
import org.jugregator.op1buddy.features.sync.ui.views.SyncTab
import org.jugregator.op1buddy.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SyncScreen(modifier: Modifier = Modifier, viewModel: OP1SyncViewModel = koinViewModel(), onBackClicked: () -> Unit) {
    var selectedTab by remember { mutableStateOf(SyncTab.Backup) }

    val title by remember {
        derivedStateOf {
            when (selectedTab) {
                SyncTab.Backup -> "Backup"
                SyncTab.Restore -> "Restore"
                SyncTab.Export -> "Export"
            }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(viewModel.backupStateFlow.collectAsState().value.backupInfo) {
        viewModel.init(context)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SyncAppBar(title = title, onBackClicked = onBackClicked)
        },
        bottomBar = {
            SyncBottomBar(selectedTab = selectedTab) {
                selectedTab = it
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            SyncTab.Backup -> {
                val uiState by viewModel.backupStateFlow.collectAsState()
                BackupScreen(
                    modifier = Modifier.padding(innerPadding),
                    state = uiState,
                    onBackupClick = { viewModel.backupDevice() },
                    onBackupSelectionChanged = {
                        viewModel.updateBackupInfo(it)
                    })
            }

            SyncTab.Restore -> {
                val uiState by viewModel.restoreStateFlow.collectAsState()
                RestoreScreen(
                    modifier = Modifier.padding(innerPadding),
                    state = uiState,
                    onRestoreClick = { viewModel.restoreDevice() },
                    onRestoreSelectionChanged = {
                        viewModel.updateRestoreInfo(it)
                    })
            }

            SyncTab.Export -> {
                val uiState by viewModel.restoreStateFlow.collectAsState()
                val isLoading by remember {
                    derivedStateOf { uiState.nowCopying }
                }
                ExportScreen(
                    modifier = Modifier.padding(innerPadding),
                    state = uiState, isCopying = isLoading, onBackupDirSelected = { uri ->
                        viewModel.onBackupExportDirSelected(context, uri)
                    })
            }
        }
    }

    /*


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
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            modifier = Modifier.alpha(isBusyAlpha),
                            contentDescription = null
                        )
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
    */
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun SyncScreenPreview() {
    AppTheme {
        SyncScreen() {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncAppBar(modifier: Modifier = Modifier, title: String, onBackClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(start = 35.dp),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 8.dp),
                onClick = onBackClicked
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(R.drawable.appbar_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
    )
}

@Preview
@Composable
private fun SyncAppBarPreview() {
    MaterialTheme {
        SyncAppBar(title = "Sync") { }
    }
}