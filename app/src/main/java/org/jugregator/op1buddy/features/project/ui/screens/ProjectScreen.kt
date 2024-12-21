package org.jugregator.op1buddy.features.project.ui.screens

import androidx.compose.foundation.basicMarquee
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jugregator.op1buddy.R
import org.jugregator.op1buddy.data.drumkit.DrumkitType
import org.jugregator.op1buddy.data.synth.SynthEngine
import org.jugregator.op1buddy.features.project.ProjectScreenViewModel
import org.jugregator.op1buddy.features.project.ui.views.ProjectBottomBar
import org.jugregator.op1buddy.features.project.ui.views.ProjectSettingsDialog
import org.jugregator.op1buddy.features.projects.ProjectSubRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectScreenViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
    onSyncClicked: (String) -> Unit,
    onDrumKitSelected: (String, Int) -> Unit,
) {

    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current

    LifecycleStartEffect(viewModel, lifecycleOwner = lifecycleOwner) {
        viewModel.loadProject()
        onStopOrDispose {
        }
    }

    val uiState by viewModel.state.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        topBar = {
            ProjectAppBar(
                title = uiState.title,
                onBackClicked = onBackClicked,
                onMoreClicked = { viewModel.onMoreClicked() },
            )
        }, bottomBar = {
            ProjectBottomBar(
                selectedTab = when {
                    checkRoute<ProjectSubRoute.SynthListRoute>(currentDestination) -> ProjectTab.Synth

                    checkRoute<ProjectSubRoute.DrumkitListListRoute>(currentDestination) -> ProjectTab.Drumkit

                    checkRoute<ProjectSubRoute.TapePlayerRoute>(currentDestination) -> ProjectTab.Tape

                    else -> ProjectTab.Synth
                }

            ) {
                when (it) {
                    ProjectTab.Synth -> {
                        navController.navigate(
                            ProjectSubRoute.SynthListRoute(projectId = uiState.projectId)
                        ) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                    ProjectTab.Drumkit -> {
                        navController.navigate(
                            ProjectSubRoute.DrumkitListListRoute(projectId = uiState.projectId)
                        ) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                    ProjectTab.Tape -> {
                        navController.navigate(
                            ProjectSubRoute.TapePlayerRoute(projectId = uiState.projectId)
                        ) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                    ProjectTab.Sync -> onSyncClicked(uiState.projectId)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = ProjectSubRoute.SynthListRoute(projectId = uiState.projectId)
        ) {

            composable<ProjectSubRoute.SynthListRoute> {
                SynthListScreen(onBackPressed = {
                    onBackClicked()
                })
            }

            composable<ProjectSubRoute.DrumkitListListRoute> {
                DrumkitListScreen(onDrumKitSelected = { projectId, drumkitIndex ->
                    onDrumKitSelected(projectId, drumkitIndex)
                }, onBackPressed = {
                    onBackClicked()
                })
            }

            composable<ProjectSubRoute.TapePlayerRoute> {
                TapePlayerScreen(onBackPressed = {
                    onBackClicked()
                })
            }
        }
    }
    if (uiState.settingDialogOpened) {
        ProjectSettingsDialog(
            projectTitle = uiState.title,
            onDismissRequest = {
                viewModel.onSettingsDialogDismiss()
            }, onConfirmation = { projectTitle ->
                viewModel.onSettingsDialogConfirm(projectTitle)
            })
    }
}

inline fun <reified T : Any> checkRoute(currentDestination: NavDestination?): Boolean {
    return currentDestination?.hierarchy?.any {
        it.hasRoute<T>()
    } == true
}

sealed class ProjectResource(val index: Int, val filename: String) {
    class Synth(index: Int, filename: String, val name: String, val engine: SynthEngine) :
        ProjectResource(index, filename)

    class Drumkit(index: Int, filename: String, val name: String, val type: DrumkitType) :
        ProjectResource(index, filename)

    class Tape(index: Int, filename: String) : ProjectResource(index, filename)
}

enum class ProjectTab {
    Synth,
    Drumkit,
    Tape,
    Sync,
}

@Preview
@Composable
fun ProjectScreenPreview() {
    MaterialTheme {
        ProjectScreen(
            onBackClicked = {},
            onSyncClicked = {},
            onDrumKitSelected = { projectId, drumkitId -> },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectAppBar(modifier: Modifier = Modifier, title: String, onBackClicked: () -> Unit, onMoreClicked: () -> Unit) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .basicMarquee(),
                text = title,
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
        actions = {
            IconButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = onMoreClicked
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(R.drawable.appbar_more),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    )
}

@Preview
@Composable
fun ProjectAppBarPreview() {
    MaterialTheme {
        ProjectAppBar(title = "Project name", onBackClicked = {}, onMoreClicked = {})
    }
}
