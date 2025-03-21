package org.jugregator.op1buddy.features.drumkit

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.features.drumkit.data.DrumkitRepository
import org.jugregator.op1buddy.features.project.data.player.TAPE_SAMPLE_RATE
import org.jugregator.op1buddy.features.projects.DrumKitRoute

class DrumKitScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val projectsRepository: ProjectsRepository,
    private val drumkitRepository: DrumkitRepository,
) : ViewModel() {
    private val _mutableState = MutableStateFlow(DrumKitUiState())
    val uiState: StateFlow<DrumKitUiState> = _mutableState

    private var minBufferSize: Int = AudioTrack.getMinBufferSize(
        DRUMKIT_SAMPLE_RATE,
        AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT
    )

    private val simplePlayer: AudioTrack = buildAudioTrack()

    private fun buildAudioTrack(): AudioTrack {
        val builder = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(TAPE_SAMPLE_RATE)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setTransferMode(AudioTrack.MODE_STREAM)
            .setBufferSizeInBytes(minBufferSize)
        @Suppress("DEPRECATION")
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder
                .setPerformanceMode(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY)

        } else {
            builder
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setFlags(AudioAttributes.FLAG_LOW_LATENCY)
                        .build()
                )
        }.build()
        return track
    }

    private val loadedSamples = mutableListOf<ByteArray>()

    private val route = savedStateHandle.toRoute<DrumKitRoute>()

    init {
        simplePlayer.play()
        viewModelScope.launch(Dispatchers.IO) {
            val project = projectsRepository.readProject(route.projectId) ?: return@launch
            val drumkit =
                drumkitRepository.loadDrumKit(project.backupDir, "drum_${route.drumkitIndex + 1}.aif") ?: return@launch
            drumkit.samples.let { samples ->
                samples.forEach { sample ->
                    loadedSamples.add(sample)
                }

                _mutableState.update { it.copy(drumCount = samples.size, title = drumkit.name) }
            }

        }
    }

    fun play(index: Int) {
        viewModelScope.launch {
            simplePlayer.flush()
            val sample = loadedSamples[index]
            for (part in sample.toList().chunked(minBufferSize)) {
                simplePlayer.write(part.toByteArray(), 0, part.size)
            }
        }
    }

    override fun onCleared() {
        simplePlayer.stop()
        simplePlayer.release()
        super.onCleared()
    }
}

data class DrumKitUiState(
    val title: String = "",
    val drumCount: Int = 0
)

private const val DRUMKIT_SAMPLE_RATE = 44100
