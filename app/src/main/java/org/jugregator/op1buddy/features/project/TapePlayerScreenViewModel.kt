package org.jugregator.op1buddy.features.project

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
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.data.project.Project
import org.jugregator.op1buddy.data.project.ProjectRepository
import org.jugregator.op1buddy.data.tape.AiffTapeMetadata
import org.jugregator.op1buddy.features.project.data.player.MultiTrackFullStopAudioTrackPlayer
import org.jugregator.op1buddy.features.projects.ProjectSubRoute
import java.io.InputStream

class TapePlayerScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val projectsRepository: ProjectsRepository,
    private val projectRepository: ProjectRepository
): ViewModel() {
    private val route = savedStateHandle.toRoute<ProjectSubRoute.TapePlayerRoute>()

    private var project: Project? = null
    private val player = MultiTrackFullStopAudioTrackPlayer()


    //TODO: rework with errors
    private val _mutableErrorFinish = MutableSharedFlow<Int>()
    val errorFinish: SharedFlow<Int> = _mutableErrorFinish


    private val _mutableState = MutableStateFlow(TapePlayerScreenState())
    val uiState: StateFlow<TapePlayerScreenState> = _mutableState

    init {
        viewModelScope.launch {
            val projectInfo = projectsRepository.readProject(route.projectId)
            if (projectInfo == null) {
                //TODO: rework, this is dirty hack to close screen
                _mutableErrorFinish.emit(1)
                return@launch
            }

            //TODO: prepare player
            val tapesData = withContext(Dispatchers.IO) {
                val tapes = projectRepository.readTapes(projectInfo)
                player.prepare(tapes.map { it.first })
                tapes
            }
            _mutableState.update {
                it.copy(tapes = tapesData)
            }
        }
    }
}

data class TapePlayerScreenState(
    val tapes: List<Pair<InputStream, AiffTapeMetadata>> = listOf()
)