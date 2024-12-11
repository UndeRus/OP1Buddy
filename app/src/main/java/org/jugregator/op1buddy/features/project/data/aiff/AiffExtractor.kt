package org.jugregator.op1buddy.features.project.data.aiff

import androidx.annotation.IntDef
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MimeTypes
import androidx.media3.common.ParserException
import androidx.media3.common.util.Assertions
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.extractor.Extractor
import androidx.media3.extractor.ExtractorInput
import androidx.media3.extractor.ExtractorOutput
import androidx.media3.extractor.PositionHolder
import androidx.media3.extractor.TrackOutput
import org.checkerframework.checker.nullness.qual.MonotonicNonNull
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

@UnstableApi
class AiffExtractor : Extractor {

    @MustBeDocumented @Retention(AnnotationRetention.SOURCE) @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.TYPE_PARAMETER
    ) @IntDef(
        STATE_READING_FILE_TYPE,
        STATE_SKIPPING_TO_SAMPLE_DATA,
        STATE_READING_SAMPLE_DATA
    )

    private annotation class State

    private var outputWriter: @MonotonicNonNull OutputWriter? = null
    private var state: @State Int = 0

    private var extractorOutput: ExtractorOutput? = null
    private var trackOutput: TrackOutput? = null
    private var inSSND = false

    private var dataStartPosition: Long = C.INDEX_UNSET.toLong()
    private var dataEndPosition: Long = C.INDEX_UNSET.toLong()

    override fun sniff(input: ExtractorInput): Boolean {
        val header = ByteArray(12)
        input.peekFully(header, 0, 12)
        // Check for "FORM AIFF" magic number
        return header[0] == 'F'.code.toByte() && header[1] == 'O'.code.toByte() &&
            header[2] == 'R'.code.toByte() && header[3] == 'M'.code.toByte() &&
            header[8] == 'A'.code.toByte() && header[9] == 'I'.code.toByte() &&
            header[10] == 'F'.code.toByte() && header[11] == 'F'.code.toByte()
    }

    override fun init(output: ExtractorOutput) {
        this.extractorOutput = output
        trackOutput = output.track(0, C.TRACK_TYPE_AUDIO)
        output.endTracks()
    }

    override fun seek(position: Long, timeUs: Long) {
        state = if (position == 0L) STATE_READING_FILE_TYPE else STATE_READING_SAMPLE_DATA
        outputWriter?.reset(timeUs)
    }

    override fun release() {
        // No resources to release
    }

    override fun read(input: ExtractorInput, seekPosition: PositionHolder): Int {
        when (state) {
            STATE_READING_FILE_TYPE -> {
                readFileType(input)
                return Extractor.RESULT_CONTINUE
            }

            STATE_CHUNK_FINISHED -> {
                readChunkType(input)
                return Extractor.RESULT_CONTINUE
            }

            STATE_SKIPPING_TO_SAMPLE_DATA -> {
                skipToSampleData(input)
                return Extractor.RESULT_CONTINUE
            }

            STATE_READING_SAMPLE_DATA -> {
                return readSampleData(input)
            }

            else -> error("Wrong read state")
        }
    }

    private fun readSampleData(input: ExtractorInput): Int {
        Assertions.checkState(dataEndPosition != C.INDEX_UNSET.toLong())
        val bytesLeft = dataEndPosition - input.position
        return if (Assertions.checkNotNull<@MonotonicNonNull OutputWriter?>(outputWriter).sampleData(input, bytesLeft))
            Extractor.RESULT_END_OF_INPUT
        else
            Extractor.RESULT_CONTINUE
    }

    private fun skipToSampleData(input: ExtractorInput) {
        val (dataStartPosition, dataSize) = AifcHeaderReader.skipToSamplelData(input)

        dataEndPosition = dataStartPosition + dataSize

        val inputLength = input.length
        if (inputLength != C.LENGTH_UNSET.toLong() && dataEndPosition > inputLength) {
            Log.w(
                TAG,
                "Data exceeds input length: $dataEndPosition, $inputLength"
            )
            dataEndPosition = inputLength
        }
        Assertions.checkNotNull<@MonotonicNonNull OutputWriter?>(outputWriter).init(
            dataStartPosition.toInt(), dataEndPosition
        )
        state = STATE_READING_SAMPLE_DATA
    }

    private fun readChunkType(input: ExtractorInput) {
        Assertions.checkState(input.position != 0L)
        val chunkType = input.readString(4)
        when (chunkType) {
            "FVER" -> {
                val size = input.readInt()
                val target = ByteArray(size)
                val format = input.readFully(target, 0, size)
                state = STATE_CHUNK_FINISHED
            }

            "COMM" -> {
                readMetadata(input)
            }

            "SSND" -> {
                readSoundMetadata(input)
            }

            else -> {
                // read size and skip
                val size = input.readInt()
                input.skipFully(size)
            }
        }
    }

    private fun readSoundMetadata(input: ExtractorInput) {
        val size = input.readInt()
        val offset = input.readInt()
        val blockSize = input.readInt()
        dataStartPosition = input.position
        dataEndPosition = input.position + size - 8
        Assertions.checkNotNull<@MonotonicNonNull OutputWriter?>(outputWriter).init(
            dataStartPosition.toInt(), dataEndPosition
        )
        state = STATE_READING_SAMPLE_DATA
    }

    private fun readMetadata(input: ExtractorInput) {
        val size = input.readInt()
        val shortBuffer = ByteArray(2)
        input.read(shortBuffer, 0, shortBuffer.size)
        val channels = hexArrayToDecimal2(shortBuffer)
        val numFrames = input.readInt()
        input.read(shortBuffer, 0, shortBuffer.size)
        val sampleSizeInBits = hexArrayToDecimal2(shortBuffer)
        input.skip(10) // 80bit float sample rate in hz
        val compressionType = input.readString(4)
        val compressionNameSizeBuffer = ByteArray(1)
        input.read(compressionNameSizeBuffer, 0, 1)
        val compressionNameSize = compressionNameSizeBuffer[0].toInt()
        val compressionName = input.readString(compressionNameSize)

        val aifcFormat = AifcFormat(
            numChannels = channels,
            //frameRateHz = 44100, //TODO: read from 80bit float
            bitsPersample = sampleSizeInBits,
            //framesPerBlock = 4096 / 2,
            //blockSize = 16384
        )

        outputWriter = AifcPassthroughOutputWriter(
            extractorOutput = extractorOutput!!,
            trackOutput = trackOutput!!,
            aifcFormat = aifcFormat,
            MimeTypes.AUDIO_RAW,
            C.ENCODING_PCM_16BIT
        )
        state = STATE_CHUNK_FINISHED
    }

    @Throws(IOException::class)
    private fun readFileType(input: ExtractorInput) {
        Assertions.checkState(input.position == 0L)
        if (dataStartPosition != C.INDEX_UNSET.toLong()) {
            input.skipFully(dataStartPosition.toInt()) // ALARM, may be overflow
            state = STATE_READING_SAMPLE_DATA
            return
        }
        if (!AifcHeaderReader.checkFileType(input)) {
            throw ParserException.createForMalformedContainer(
                "Unsupported or unrecognized aiff file type", null
            )
        }
        input.skipFully((input.peekPosition - input.position).toInt())
        state = STATE_CHUNK_FINISHED
    }

    companion object {
        private const val STATE_READING_FILE_TYPE: Int = 0
        private const val STATE_CHUNK_FINISHED = 2
        private const val STATE_SKIPPING_TO_SAMPLE_DATA: Int = 7

        private
        const val STATE_READING_SAMPLE_DATA: Int = 8

        val TAG = "AiffExtractor"

        const val TARGET_SAMPLES_PER_SECOND = 10
    }
}

const val SIZE_OF_INT = 4

@OptIn(UnstableApi::class)
fun ExtractorInput.readInt(): Int {
    val buffer = ByteArray(SIZE_OF_INT)
    readFully(buffer, 0, SIZE_OF_INT)
    return ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN).int
}

@OptIn(UnstableApi::class)
fun ExtractorInput.readString(length: Int): String {
    val bytes = ByteArray(length)
    readFully(bytes, 0, length)
    return String(bytes, Charsets.US_ASCII)
}

fun hexArrayToDecimal2(array: ByteArray): Int {
    val byte0 = array[0].toInt()
    val byte1 = array[1].toInt()
    val result = (byte0 shl 8) + byte1
    return result
}

fun hexArrayToDecimal3(array: ByteArray): Int {
    val byte0 = array[0].toInt()
    val byte1 = array[1].toInt()
    val byte3 = array[2].toInt()
    val result = (byte0 shl 16) + (byte1 shl 8) + byte3
    return result
}

fun hexArrayToDecimal(array: ByteArray): Int {
    return ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).getInt()
}
