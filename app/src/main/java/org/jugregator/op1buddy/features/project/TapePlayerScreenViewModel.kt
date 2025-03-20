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
import org.jugregator.op1buddy.data.LCE
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
            _mutableState.update { it.copy(data = LCE.Loading) }

            val projectInfo = projectsRepository.readProject(route.projectId)
            if (projectInfo == null) {
                //TODO: rework, this is dirty hack to close screen
                _mutableState.update { it.copy(data = LCE.Error) }
                _mutableErrorFinish.emit(1)
                return@launch
            }
            project = projectInfo

            val tapesData = withContext(Dispatchers.IO) {
                val tapes = projectRepository.readTapes(projectInfo)
                player.prepare(tapes.map { it.first })
                tapes
            }

            val maxLength = withContext(Dispatchers.Default) {
                tapesData.map { it.second.regions.regions }
                    .maxByOrNull { it.maxBy { it.second }.second }
                    ?.maxByOrNull { it.second }?.second ?: 10L
            }
            _mutableState.update {
                it.copy(data = LCE.Content(tapesData), maxLength = maxLength)
            }

        }
    }

    fun play() {
        player.play()
        _mutableState.update { it.copy(isPlaying = true) }
        viewModelScope.launch {
            while (player.isPlaying) {
                val newPosition = player.getPosition()
                if (newPosition >= uiState.value.maxLength) {
                    stop()
                    return@launch
                }
                _mutableState.update {
                    it.copy(position = newPosition)
                }
                delay(PROGRESS_TIMEOUT_MILLIS)
            }
        }
    }

    fun pause() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                player.pause()
            }
            _mutableState.update { it.copy(isPlaying = false) }

        }
    }

    fun stop() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                player.stop()
            }
        }
        _mutableState.update { it.copy(position = 0L, isPlaying = false) }
    }

    fun seekToSample(sampleIndex: Long, move: Boolean) {
        _mutableState.update { it.copy(position = sampleIndex) }
        if (move) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    player.seek(sampleIndex)
                }
            }
        }
    }

    fun toggleTrack(index: Int, checked: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                player.toggleTrack(index, checked)
            }
        }
    }
}

private const val PROGRESS_TIMEOUT_MILLIS: Long = 100

data class TapePlayerScreenState(
    val position: Long = 0,
    val maxLength: Long = 0,
    val data: LCE<List<Pair<InputStream, AiffTapeMetadata>>> = LCE.Loading,
    val isPlaying: Boolean = false,
)
