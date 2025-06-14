package org.jugregator.op1buddy.features.project.data.player

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import java.io.InputStream
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class MultiTrackFullStopAudioTrackPlayer {

    private var inputStreams = emptyList<InputStream>()

    @Volatile
    private var enabledTracks = arrayOf<Boolean>()

    private var minBufferSize: Int = AudioTrack.getMinBufferSize(
        TAPE_SAMPLE_RATE,
        AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT
    )

    private var audioTrack: AudioTrack? = null
    private var playbackThread: Thread? = null

    private var seekPositionBytes: AtomicLong = AtomicLong(0L)
    private var seekPositionInSamples
        get() = seekPositionBytes.get() / BYTES_PER_SAMPLE
        set(value) = seekPositionBytes.set(value * BYTES_PER_SAMPLE)

    private val playLock = Object()

    private var playerState = AtomicReference(PlayerState.Stopped)

    val isPlaying
        get() = playerState.get() == PlayerState.Playing

    fun getPosition(): Long {
        return seekPositionInSamples
    }

    private sealed class PlayerAction {
        object Stop : PlayerAction()
        object Play : PlayerAction()
        object Pause : PlayerAction()
        class Seek(val offsetInSamples: Long) : PlayerAction()
    }

    private fun emitAction(action: PlayerAction) {
        when (playerState.get()) {
            PlayerState.Stopped -> {
                when (action) {
                    PlayerAction.Play -> {
                        playbackThread = Thread(::playAudio).apply {
                            priority = Thread.MIN_PRIORITY
                            start()
                        }
                        playerState.set(PlayerState.Playing)
                    }

                    is PlayerAction.Seek -> {
                        // Seek to position
                        seekToSample(action.offsetInSamples)
                    }

                    PlayerAction.Stop, PlayerAction.Pause -> {
                        //Do nothing
                    }
                }
            }

            PlayerState.Playing -> {
                when (action) {
                    PlayerAction.Stop -> {
                        playerState.set(PlayerState.Stopped)
                        synchronized(playLock) {
                            fullStopPlaying()
                        }
                        seekToSample(0)
                    }

                    PlayerAction.Pause -> {
                        playerState.set(PlayerState.Paused)
                        synchronized(playLock) {
                            fullStopPlaying()
                        }
                    }

                    is PlayerAction.Seek -> {
                        synchronized(playLock) {
                            fullStopPlaying()
                        }
                        seekToSample(action.offsetInSamples)
                        playerState.set(PlayerState.Playing)
                    }

                    PlayerAction.Play -> {
                        // We're already playing, do nothing
                    }
                }
            }

            PlayerState.Paused -> {
                when (action) {
                    PlayerAction.Play -> {
                        playbackThread = Thread(::playAudio).apply {
                            start()
                            priority = Thread.MIN_PRIORITY
                        }
                        playerState.set(PlayerState.Playing)
                    }

                    is PlayerAction.Seek -> {
                        seekToSample(action.offsetInSamples)
                    }

                    PlayerAction.Pause -> {
                        // Already paused do nothing
                    }

                    PlayerAction.Stop -> {
                        seekToSample(0)
                        playerState.set(PlayerState.Stopped)
                    }
                }
            }

            null -> {}
        }
    }

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

    fun prepare(inputStreams: List<InputStream>) {
        this.inputStreams = inputStreams
        enabledTracks = MutableList(inputStreams.size) { true }.toTypedArray()
    }

    fun play() {
        emitAction(PlayerAction.Play)
    }

    fun pause() {
        emitAction(PlayerAction.Pause)
    }

    fun stop() {
        emitAction(PlayerAction.Stop)
    }

    fun seek(offsetInSamples: Long) {
        emitAction(PlayerAction.Seek(offsetInSamples))
    }

    @Synchronized
    private fun fullStopPlaying() {
        playbackThread?.interrupt()
        playbackThread = null

        audioTrack?.let {
            if (it.playState == AudioTrack.PLAYSTATE_PLAYING || it.playState == AudioTrack.PLAYSTATE_PAUSED) {
                it.stop()
            }
            it.release()
        }
        audioTrack = null
    }

    fun toggleTrack(trackIndex: Int, enabled: Boolean) {
        if (trackIndex < enabledTracks.size) {
            enabledTracks[trackIndex] = enabled
        }
    }

    private fun seekToTime(milliseconds: Long) {
        val byteOffset = (milliseconds / 1000.0 * BYTES_PER_SECOND).toLong()
        seekToByte(byteOffset)
    }

    private fun seekToSample(sampleNumber: Long) {
        val byteOffset = sampleToBytes(sampleNumber)
        seekToByte(byteOffset)
    }

    private fun seekToByte(byteOffset: Long) {
        seekPositionBytes.set(byteOffset)
        //positionListener?.invoke(seekPositionInSamples)
        for (inputStream in inputStreams) {
            inputStream.reset()
            inputStream.skip(byteOffset)
        }
    }

    private fun playAudio() {
        audioTrack = buildAudioTrack()
        audioTrack?.play()

        val buffers = inputStreams.map {
            ByteArray(minBufferSize)
        }.toTypedArray()

        val resultBuffer = ByteArray(minBufferSize)

        synchronized(playLock) {
            try {
                while (playerState.get() == PlayerState.Playing) {
                    var belowZero = 0
                    var maxReadSize = 0
                    for (i in inputStreams.indices) {
                        val readSize = inputStreams[i].read(buffers[i], 0, buffers[i].size)
                        if (readSize < 0) {
                            belowZero += 1
                        }
                        if (readSize > maxReadSize) {
                            maxReadSize = readSize
                        }
                    }
                    if (belowZero == inputStreams.size) {
                        // All streams are finished
                        break
                    }
                    seekPositionBytes.addAndGet(maxReadSize.toLong())

                    mixBuffers(resultBuffer, buffers, enabledTracks)
                    audioTrack?.write(resultBuffer, 0, maxReadSize, AudioTrack.WRITE_BLOCKING)
                }
            } catch (e: InterruptedException) {
                //TODO: interrupt during stop
            }
        }
        fullStopPlaying()
    }

    private fun seekInStream(inputStream: InputStream, seekPositionBytes: Long) {
        inputStream.reset()
        inputStream.skip(seekPositionBytes)
    }
}

enum class PlayerState {
    Stopped,
    Playing,
    Paused
}

private fun sampleToBytes(sampleNumber: Long): Long {
    return sampleNumber * BYTES_PER_SAMPLE
}

private fun bytesToSamples(bytesOffset: Long): Long {
    return bytesOffset / BYTES_PER_SAMPLE
}

fun mixBuffers(resultBuffer: ByteArray, buffers: Array<ByteArray>, enabled: Array<Boolean>) {
    assert(buffers.all { it.size == resultBuffer.size })
    for (i in resultBuffer.indices step 2) {
        // Convert bytes to PCM samples
        var sample = 0
        for (b in buffers.indices) {
            if (enabled[b]) {
                sample += (buffers[b][i + 1].toInt() shl 8) or (buffers[b][i].toInt() and 0xFF)
            }
        }

        // Mix samples and clamp to valid range
        val mixedSample = sample.coerceIn(-32768, 32767)

        // Convert PCM sample back to bytes
        resultBuffer[i] = (mixedSample and 0xFF).toByte()
        resultBuffer[i + 1] = (mixedSample shr 8 and 0xFF).toByte()
    }
}

const val TAPE_SAMPLE_RATE = 44100
private const val CHANNELS = 1 // Mono
private const val BIT_DEPTH = 16 // 16-bit PCM
private const val BYTES_PER_SAMPLE = (BIT_DEPTH / 8) * CHANNELS
private const val BYTES_PER_SECOND = BYTES_PER_SAMPLE * TAPE_SAMPLE_RATE
