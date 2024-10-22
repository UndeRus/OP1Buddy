package org.jugregator.op1buddy.features.projects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.jugregator.op1buddy.Project
import org.jugregator.op1buddy.features.project.ui.screens.ProjectScreen
import org.jugregator.op1buddy.features.projects.ui.screens.ProjectsScreen
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.features.sync.ui.screens.SyncScreen
import org.jugregator.op1buddy.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.KoinContext

@Serializable
object ProjectsRoute

@Serializable
data class ProjectRoute(val projectId: String)

@Serializable
data class SyncRoute(val projectId: String)

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
                                ProjectsScreen({ project ->
                                    navController.navigate(ProjectRoute(project.id))
                                })
                            }


                            composable<ProjectRoute> {
                                ProjectScreen(onSyncClicked = { projectId ->
                                    navController.navigate(SyncRoute(projectId))
                                })
                            }
                            composable<SyncRoute> {
                                /*
                                val sync = backStackEntry.toRoute<SyncRoute>()
                                val context = LocalContext.current
                                */
                                SyncScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
