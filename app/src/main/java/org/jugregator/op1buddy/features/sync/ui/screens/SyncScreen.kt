package org.jugregator.op1buddy.features.sync.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.features.sync.ui.views.SyncBottomBar
import org.jugregator.op1buddy.features.sync.ui.views.SyncTab
import org.jugregator.op1buddy.ui.icons.Icons
import org.jugregator.op1buddy.ui.icons.appbar.Back
import org.jugregator.op1buddy.ui.icons.background.RightTop
import org.jugregator.op1buddy.ui.icons.background.LeftBottom
import org.jugregator.op1buddy.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SyncScreen(
    modifier: Modifier = Modifier,
    viewModel: OP1SyncViewModel = koinViewModel(),
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LifecycleStartEffect(viewModel, lifecycleOwner = lifecycleOwner) {
        viewModel.init(context)
        onStopOrDispose {
            viewModel.deInit()
        }
    }

    var selectedTab by remember { mutableStateOf(SyncTab.Backup) }

    
    val backupState by viewModel.backupStateFlow.collectAsState()
    val restoreState by viewModel.restoreStateFlow.collectAsState()

    val interractionEnabled by remember {
        derivedStateOf {
            !backupState.nowCopying && !restoreState.nowCopying
        }
    }

    val title by remember {
        derivedStateOf {
            when (selectedTab) {
                SyncTab.Backup -> context.getString(R.string.backup_tab_title)
                SyncTab.Restore -> context.getString(R.string.restore_tab_title)
                SyncTab.Export -> context.getString(R.string.export_tab_title)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SyncAppBar(title = title, enabled = interractionEnabled, onBackClicked = onBackClicked)
        },
        bottomBar = {
            SyncBottomBar(selectedTab = selectedTab, enabled = interractionEnabled) {
                selectedTab = it
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Image(
                imageVector = Icons.Background.LeftBottom,
                modifier = Modifier.align(Alignment.BottomStart),
                contentDescription = null,
            )

            Image(
                imageVector = Icons.Background.RightTop,
//                painterResource(R.drawable.background_right_top),
                modifier = Modifier.align(Alignment.TopEnd),
                contentDescription = null,
            )

            when (selectedTab) {
                SyncTab.Backup -> {
                    val uiState by viewModel.backupStateFlow.collectAsState()
                    BackupScreen(
                        state = uiState,
                        onBackupClick = { viewModel.backupDevice() },
                        onBackupSelectionChanged = {
                            viewModel.updateBackupInfo(it)
                        })
                }

                SyncTab.Restore -> {
                    val uiState by viewModel.restoreStateFlow.collectAsState()
                    RestoreScreen(
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
                        state = uiState,
                        isCopying = isLoading,
                        onBackupDirSelected = { uri ->
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
        SyncScreen {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncAppBar(modifier: Modifier = Modifier, title: String, enabled: Boolean, onBackClicked: () -> Unit) {
    TopAppBar(
        modifier = modifier,
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
                onClick = onBackClicked,
                enabled = enabled,
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icons.AppBar.Back,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        },
    )
}

@Preview
@Composable
fun SyncAppBarPreview() {
    MaterialTheme {
        SyncAppBar(title = "Sync", enabled = true) { }
    }
}