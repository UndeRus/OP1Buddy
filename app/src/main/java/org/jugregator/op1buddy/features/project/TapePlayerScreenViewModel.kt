package org.jugregator.op1buddy.features.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
) : ViewModel() {
    private val route = savedStateHandle.toRoute<ProjectSubRoute.TapePlayerRoute>()

    private var project: Project? = null

    private val player = MultiTrackFullStopAudioTrackPlayer()

    //TODO: rework with errors
    private val _mutableErrorFinish = MutableSharedFlow<Int>()

    val errorFinish: SharedFlow<Int> = _mutableErrorFinish
    private val _mutableState = MutableStateFlow(TapePlayerScreenState())

    val uiState: StateFlow<TapePlayerScreenState> = _mutableState

    fun loadTapes() {
        viewModelScope.launch {
            val projectInfo = projectsRepository.readProject(route.projectId)
            if (projectInfo == null) {
                //TODO: rework, this is dirty hack to close screen
                _mutableErrorFinish.emit(1)
                return@launch
            }
            project = projectInfo

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

    fun play() {
        player.play()
        viewModelScope.launch {
            while (player.isPlaying) {
                _mutableState.update {
                    it.copy(position = player.getPosition())
                }
                delay(100)
            }
        }
    }

    fun pause() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                player.pause()
            }
        }
    }

    fun stop() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                player.stop()
            }
        }
        _mutableState.update { it.copy(position = 0L) }
    }

    fun toggleTrack(index: Int, checked: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                player.toggleTrack(index, checked)
            }
        }
    }
}

data class TapePlayerScreenState(
    val tapes: List<Pair<InputStream, AiffTapeMetadata>> = listOf(),
    val position: Long = 0
)
