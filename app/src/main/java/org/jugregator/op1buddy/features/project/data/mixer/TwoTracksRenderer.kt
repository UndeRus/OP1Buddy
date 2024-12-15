package org.jugregator.op1buddy.features.project.data.mixer

import android.content.Context
import android.os.Handler
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MimeTypes
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.common.util.Clock
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.MediaClock
import androidx.media3.exoplayer.Renderer
import androidx.media3.exoplayer.RendererCapabilities
import androidx.media3.exoplayer.RendererConfiguration
import androidx.media3.exoplayer.StandaloneMediaClock
import androidx.media3.exoplayer.audio.AudioRendererEventListener
import androidx.media3.exoplayer.audio.AudioSink
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.TrackGroupArray
import androidx.media3.exoplayer.trackselection.ExoTrackSelection
import androidx.media3.exoplayer.trackselection.FixedTrackSelection
import androidx.media3.exoplayer.trackselection.TrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelectorResult
import java.util.ArrayDeque

@UnstableApi
class AudioRendererWithoutClock(
    context: Context,
    mediaCodecSelector: MediaCodecSelector
) :
    MediaCodecAudioRenderer(context, mediaCodecSelector) {

    override fun getMediaClock(): MediaClock? {
        return null
    }
}

@UnstableApi
class TwoTracksRenderersFactory(context: Context) : DefaultRenderersFactory(context) {
    override fun buildAudioRenderers(
        context: Context,
        extensionRendererMode: Int,
        mediaCodecSelector: MediaCodecSelector,
        enableDecoderFallback: Boolean,
        audioSink: AudioSink,
        eventHandler: Handler,
        eventListener: AudioRendererEventListener,
        out: ArrayList<Renderer>
    ) {
        super.buildAudioRenderers(
            context,
            extensionRendererMode,
            mediaCodecSelector,
            enableDecoderFallback,
            audioSink,
            eventHandler,
            eventListener,
            out
        )

        out.add(AudioRendererWithoutClock(context, mediaCodecSelector))
        out.add(AudioRendererWithoutClock(context, mediaCodecSelector))
        out.add(AudioRendererWithoutClock(context, mediaCodecSelector))
        out.add(AudioRendererWithoutClock(context, mediaCodecSelector))
    }
}

@OptIn(UnstableApi::class)
class TwoTracksTrackSelector : TrackSelector() {
    override fun selectTracks(
        rendererCapabilities: Array<out RendererCapabilities>,
        trackGroups: TrackGroupArray,
        periodId: MediaSource.MediaPeriodId,
        timeline: Timeline
    ): TrackSelectorResult {
        val audioRenderers = ArrayDeque<Int>()
        val configs = arrayOfNulls<RendererConfiguration>(rendererCapabilities.size)
        val selections = arrayOfNulls<ExoTrackSelection>(rendererCapabilities.size)
        for (i in rendererCapabilities.indices) {
            if (rendererCapabilities[i].trackType == C.TRACK_TYPE_AUDIO) {
                audioRenderers.add(i)
            }
        }

        for (i in 0 until trackGroups.length) {
            if (MimeTypes.isAudio(trackGroups.get(i).getFormat(0).sampleMimeType)) {
                val index = audioRenderers.poll()
                if (index != null) {
                    selections[index] = FixedTrackSelection(trackGroups.get(i), 0)
                    configs[index] = RendererConfiguration.DEFAULT
                }
            }
        }
        return TrackSelectorResult(configs, selections, Tracks.EMPTY, Any())
    }

    override fun onSelectionActivated(info: Any?) = Unit
}