package org.jugregator.op1buddy.features.projects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.features.sync.ui.SyncScreen
import org.jugregator.op1buddy.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@Serializable
object ProjectsRoute

@Serializable
data class SyncRoute(val projectId: Int)

class MainActivity : ComponentActivity() {
    private val viewModel: OP1SyncViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
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
                                navController.navigate(SyncRoute(project.id))
                            })
                        }

                        composable<SyncRoute> { backStackEntry ->

                            val sync = backStackEntry.toRoute<SyncRoute>()
                            val context = LocalContext.current
                            viewModel.init(context)
                            SyncScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectsScreen(onProjectClicked: (Project) -> Unit, modifier: Modifier = Modifier) {
    Column {
        ProjectScreenTitle(modifier = Modifier.fillMaxWidth())
        val projects by remember { mutableStateOf(getProjects()) }
        LazyColumn(modifier = modifier) {
            items(projects, key = { project -> project.id }) { project ->
                ProjectItem(project = project, onClick = {
                    onProjectClicked(project)
                })
            }
        }
    }
}

@Preview
@Composable
fun ProjectsScreenPreview() {
    AppTheme {
        ProjectsScreen({})
    }
}

@Composable fun ProjectScreenTitle(modifier: Modifier = Modifier) {
    Text(
        text = "Companion for OP-1",
        modifier = modifier,
        style = MaterialTheme.typography.displayLarge.copy(color = MaterialTheme.colorScheme.tertiary),
        textAlign = TextAlign.Center
    )
}

@Composable
fun ProjectItem(project: Project, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(8.dp)) {
        ProjectNumber(id = project.id, onClick = onClick)
        ProjectTitle(
            title = project.title,
            onClick = onClick,
            modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun ProjectNumber(id: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        shape = CircleShape,
        onClick = onClick,
        modifier = modifier.size(80.dp)
    ) {
        Text(
            text = id.toString(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 25.sp)
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable fun ProjectTitle(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(onClick = onClick, modifier = modifier.height(80.dp)) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 25.sp)
        )
    }
}

@Preview
@Composable
fun ProjectItemPreview() {
    val project = Project(2, "My long sommer jam at sunset")
    AppTheme {
        ProjectItem(project, {})
    }
}

private fun getProjects(): List<Project> {
    return listOf(
        Project(1, "My long sommer jam at sunset"),
        Project(2, "8-bit jam at NAMM"),
        Project(3, "All In Line"),
        Project(4, "A Time Of Shimmer"),
        Project(5, "Investors demo at HOR"),
        Project(6, "It's Time To Have Fun With Motion"),
        Project(7, "Chill dream"),
        Project(8, "Hurr durr herp derp"),
    )
}