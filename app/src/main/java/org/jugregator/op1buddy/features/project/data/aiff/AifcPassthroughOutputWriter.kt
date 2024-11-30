package org.jugregator.op1buddy.features.project.data.aiff

import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.C.PcmEncoding
import androidx.media3.common.Format
import androidx.media3.common.ParserException
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.extractor.Extractor
import androidx.media3.extractor.ExtractorInput
import androidx.media3.extractor.ExtractorOutput
import androidx.media3.extractor.TrackOutput
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

@OptIn(UnstableApi::class)
class AifcPassthroughOutputWriter(
    private val extractorOutput: ExtractorOutput,
    private val trackOutput: TrackOutput,
    private val aifcFormat: AifcFormat,
    mimeType: String?,
    pcmEncoding: @PcmEncoding Int
) : OutputWriter {
    private val format: Format

    /** The target size of each output sample, in bytes.  */
    private val targetSampleSizeBytes: Int

    /** The time at which the writer was last [.reset].  */
    private var startTimeUs: Long = 0

    /**
     * The number of bytes that have been written to [.trackOutput] but have yet to be
     * included as part of a sample (i.e. the corresponding call to [ ][TrackOutput.sampleMetadata] has yet to be made).
     */
    private var pendingOutputBytes = 0

    /**
     * The total number of frames in samples that have been written to the trackOutput since the
     * last call to [.reset].
     */
    private var outputFrameCount: Long = 0

    init {
        val bytesPerFrame = aifcFormat.numChannels * aifcFormat.bitsPersample / 8
        // Validate the WAV format. Blocks are expected to correspond to single frames.
        if (aifcFormat.blockSize != bytesPerFrame) {
            throw ParserException.createForMalformedContainer(
                "Expected block size: " + bytesPerFrame + "; got: " + aifcFormat.blockSize,  /* cause= */
                null
            )
        }

        val constantBitrate = aifcFormat.frameRateHz * bytesPerFrame * 8
        targetSampleSizeBytes =
            max(
                bytesPerFrame.toDouble(),
                (aifcFormat.frameRateHz * bytesPerFrame / AiffExtractor.TARGET_SAMPLES_PER_SECOND).toDouble()
            )
                .toInt()
        format =
            Format.Builder()
                .setSampleMimeType(mimeType)
                .setAverageBitrate(constantBitrate)
                .setPeakBitrate(constantBitrate)
                .setMaxInputSize(targetSampleSizeBytes)
                .setChannelCount(aifcFormat.numChannels)
                .setSampleRate(aifcFormat.frameRateHz)
                .setPcmEncoding(pcmEncoding)
                .build()
    }

    override fun reset(timeUs: Long) {
        startTimeUs = timeUs
        pendingOutputBytes = 0
        outputFrameCount = 0
    }

    override fun init(dataStartPosition: Int, dataEndPosition: Long) {
        extractorOutput.seekMap(
            AifcSeekMap(aifcFormat,  /* framesPerBlock= */1, dataStartPosition.toLong(), dataEndPosition)
        )
        trackOutput.format(format)
    }

    @Throws(IOException::class)
    override fun sampleData(input: ExtractorInput, bytesLeft: Long): Boolean {
        // Write sample data until we've reached the target sample size, or the end of the data.
        var bytesLeft = bytesLeft
        while (bytesLeft > 0 && pendingOutputBytes < targetSampleSizeBytes) {
            val bytesToRead = min((targetSampleSizeBytes - pendingOutputBytes).toLong(), bytesLeft).toInt()
            val bytesAppended = trackOutput.sampleData(input, bytesToRead, true)
            if (bytesAppended == Extractor.RESULT_END_OF_INPUT) {
                bytesLeft = 0
            } else {
                pendingOutputBytes += bytesAppended
                bytesLeft -= bytesAppended.toLong()
            }
        }

        // Write the corresponding sample metadata. Samples must be a whole number of frames. It's
        // possible that the number of pending output bytes is not a whole number of frames if the
        // stream ended unexpectedly.
        val bytesPerFrame = aifcFormat.blockSize
        val pendingFrames = pendingOutputBytes / bytesPerFrame
        if (pendingFrames > 0) {
            val timeUs =
                (startTimeUs
                    + Util.scaleLargeTimestamp(
                    outputFrameCount, C.MICROS_PER_SECOND, aifcFormat.frameRateHz.toLong()
                ))
            val size = pendingFrames * bytesPerFrame
            val offset = pendingOutputBytes - size
            trackOutput.sampleMetadata(
                timeUs, C.BUFFER_FLAG_KEY_FRAME, size, offset,  /* cryptoData= */null
            )
            outputFrameCount += pendingFrames.toLong()
            pendingOutputBytes = offset
        }

        return bytesLeft <= 0
    }
}
