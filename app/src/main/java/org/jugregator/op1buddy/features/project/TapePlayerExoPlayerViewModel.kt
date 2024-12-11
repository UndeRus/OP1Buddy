package org.jugregator.op1buddy.features.project

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.AssetDataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.extractor.ExtractorsFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jugregator.op1buddy.features.drumkit.media.ExoPlayerProvider
import org.jugregator.op1buddy.features.project.data.aiff.AiffExtractor
import org.jugregator.op1buddy.features.project.data.mixer.TwoTracksRenderersFactory
import org.jugregator.op1buddy.features.project.data.mixer.TwoTracksTrackSelector
import org.jugregator.op1buddy.features.project.ui.screens.ProjectResource
import java.io.File

class TapePlayerExoPlayerViewModel(
    private val context: Context,
    private val exoPlayerProvider: ExoPlayerProvider,

    ) : ViewModel() {
    private val _mutableUiState = MutableStateFlow(TapePlayerState())
    val uiState: StateFlow<TapePlayerState> = _mutableUiState

    //private val player = exoPlayerProvider.createExoPlayerMultitrack()
    private lateinit var player: ExoPlayer

    @UnstableApi
    fun play() {
        //val mediaItem = MediaItem.fromUri("asset:///tape1.aif")
        val assetDataSource = AssetDataSource(context)

        val defaultExtractorsFactory = DefaultExtractorsFactory()

        val aifExtractorsFactory = ExtractorsFactory {
            arrayOf(AiffExtractor())
        }

        //val dataSourceFactory: () -> DataSource = { assetDataSource }
        val dataSourceFactory = DefaultDataSource.Factory(context)

        val mediaSourceFactory = ProgressiveMediaSource.Factory(
            dataSourceFactory,
            aifExtractorsFactory
        )

//        val mediaItem1 = MediaItem.fromUri(Uri.parse("asset:///tape1.aif"))
//        val mediaItem2 = MediaItem.fromUri(Uri.parse("asset:///tape2.aif"))
//        val mediaItem1 = MediaItem.fromUri(Uri.fromFile(File(context.filesDir, "tapes/tape1.mp3")))
//        val mediaItem2 = MediaItem.fromUri(Uri.fromFile(File(context.filesDir, "tapes/tape2.mp3")))

        val mediaItem1 = MediaItem.fromUri(Uri.fromFile(File(context.filesDir, "tapes/tape1.aif")))
        val mediaItem2 = MediaItem.fromUri(Uri.fromFile(File(context.filesDir, "tapes/tape2.aif")))

        val mediaSource1 = mediaSourceFactory.createMediaSource(mediaItem1)
        val mediaSource2 = mediaSourceFactory.createMediaSource(mediaItem2)
        val mediaSource = MergingMediaSource(mediaSource1, mediaSource2)


        val renderersFactory = TwoTracksRenderersFactory(context)

        val trackSelector = TwoTracksTrackSelector()

        player = ExoPlayer
            .Builder(context, renderersFactory)
            .setTrackSelector(trackSelector)
            .build()
        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()

        /*
        player.setMediaSource(mediaSource)
        */
        player.prepare()
// Start the playback.
        player.play()
    }

    fun stop() {
        player.stop()
    }
}

data class TapePlayerState(
    val tapes: List<ProjectResource.Tape> = listOf()
)
