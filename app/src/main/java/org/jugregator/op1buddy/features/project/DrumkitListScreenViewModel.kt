package org.jugregator.op1buddy.features.project

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jugregator.op1buddy.data.LCE
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.data.drumkit.DrumkitInfo
import org.jugregator.op1buddy.data.project.Project
import org.jugregator.op1buddy.data.project.ProjectRepository
import org.jugregator.op1buddy.features.projects.ProjectSubRoute

class DrumkitListScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val projectsRepository: ProjectsRepository,
    private val projectRepository: ProjectRepository,
) : ViewModel() {

    private var _mutableState = MutableStateFlow(DrumkitListScreenState())
    val uiState: StateFlow<DrumkitListScreenState> = _mutableState

    private val route = savedStateHandle.toRoute<ProjectSubRoute.DrumkitListListRoute>()

    //TODO: rework with errors
    private val _mutableErrorFinish = MutableSharedFlow<Int>()
    val errorFinish: SharedFlow<Int> = _mutableErrorFinish

    fun loadDrumkits() {
        viewModelScope.launch {
            _mutableState.update { it.copy(data = LCE.Loading) }
            val project = withContext(Dispatchers.IO) {
                loadProject()
            }
            if (project == null) {
                //TODO: rework, this is dirty hack to close screen
                _mutableState.update { it.copy(data = LCE.Error) }
                _mutableErrorFinish.emit(1)
                return@launch
            }

            loadDrumkits(project)
        }
    }

    private suspend fun loadDrumkits(project: Project) {
        val drumkits = withContext(Dispatchers.IO) {
            projectRepository.readDrumKits(project)
        }
        _mutableState.update { it.copy(data = LCE.Content(drumkits)) }
    }

    private suspend fun loadProject(): Project? {
        return projectsRepository.readProject(route.projectId)
    }

    fun onDrumKitSelected(drumkitIndex: Int, callback: (String, Int) -> Unit) {
        callback(route.projectId, drumkitIndex)
    }
}

@Immutable
data class DrumkitListScreenState(
    val data: LCE<List<DrumkitInfo>> = LCE.Loading
)
