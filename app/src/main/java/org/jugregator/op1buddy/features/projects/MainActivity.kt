package org.jugregator.op1buddy.features.projects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.jugregator.op1buddy.features.drumkit.ui.screen.DrumKitScreen
import org.jugregator.op1buddy.features.project.ui.screens.ProjectScreen
import org.jugregator.op1buddy.features.projects.ui.screens.ProjectsScreen
import org.jugregator.op1buddy.features.sync.ui.screens.SyncScreen
import org.jugregator.op1buddy.ui.theme.AppTheme
import org.koin.compose.KoinContext

@Serializable
object ProjectsRoute

@Serializable
data class ProjectRoute(val isNew: Boolean = false, val projectId: String? = null)

@Serializable
data class SyncRoute(val projectId: String)

@Serializable
data class DrumKitRoute(val projectId: String, val drumkitIndex: Int)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            KoinContext {
                AppTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = ProjectsRoute,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable<ProjectsRoute> {
                                ProjectsScreen(onProjectClicked = { project ->
                                    navController.navigate(ProjectRoute(projectId = project.id))
                                }, onNewProjectClicked = {
                                    navController.navigate(ProjectRoute(isNew = true))
                                })
                            }


                            composable<ProjectRoute> {
                                ProjectScreen(onSyncClicked = { projectId ->
                                    navController.navigate(SyncRoute(projectId))
                                }, onDrumKitSelected = { projectId, drumkitId ->
                                    navController.navigate(
                                        DrumKitRoute(
                                            projectId = projectId,
                                            drumkitIndex = drumkitId
                                        )
                                    )
                                })
                            }
                            composable<SyncRoute> {
                                SyncScreen()
                            }

                            composable<DrumKitRoute> {
                                DrumKitScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
