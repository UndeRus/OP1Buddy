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
import org.jugregator.op1buddy.data.project.Project
import org.jugregator.op1buddy.data.project.ProjectRepository
import org.jugregator.op1buddy.data.synth.SynthInfo
import org.jugregator.op1buddy.features.projects.ProjectSubRoute

class SynthListScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val projectsRepository: ProjectsRepository,
    private val projectRepository: ProjectRepository,
): ViewModel() {
    private val route = savedStateHandle.toRoute<ProjectSubRoute.SynthListRoute>()

    //TODO: rework with errors
    private val _mutableErrorFinish = MutableSharedFlow<Int>()
    val errorFinish: SharedFlow<Int> = _mutableErrorFinish

    private var _mutableState = MutableStateFlow(SynthListScreenState())
    val uiState: StateFlow<SynthListScreenState> = _mutableState

    fun loadSynths() {
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

            //TODO: export to separate function
            val synths = withContext(Dispatchers.IO) {
                projectRepository.readSynths(project)
            }
            _mutableState.update { it.copy(data = LCE.Content(synths)) }
        }
    }


    private suspend fun loadProject(): Project? {
        return projectsRepository.readProject(route.projectId)
    }
}

@Immutable
data class SynthListScreenState(
    val data: LCE<List<SynthInfo>> = LCE.Loading
)
