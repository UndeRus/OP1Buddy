package org.jugregator.op1buddy.features.project.data.aiff

import androidx.media3.common.util.ParsableByteArray
import androidx.media3.common.util.UnstableApi
import androidx.media3.extractor.ExtractorInput
import java.io.IOException

@UnstableApi
class AifcHeaderReader {

    companion object {

        @JvmStatic
        fun checkFileType(input: ExtractorInput): Boolean {
            val scratch = ParsableByteArray(AIFC_HEADER_SIZE)
            val chunkHeader = ChunkHeader.peek(input, scratch)
            if (chunkHeader.form != AifcUtil.FORM && chunkHeader.type != AifcUtil.AIFC) {
                return false
            }
            return true
        }

        @JvmStatic
        @Throws(IOException::class)
        fun skipToSamplelData(input: ExtractorInput): Pair<Long, Long> {
            input.resetPeekPosition()

            val scratch = ParsableByteArray(AIFC_HEADER_SIZE)
            val chunkHeader = ChunkHeader.peek(input, scratch)

            var ssndChunkFound = false
            var chunkName: String
            var chunkSize: Int
            while (!ssndChunkFound) {
                chunkName = input.readString(4)
                chunkSize = input.readInt()
                if (chunkName == "SSND") {
                    ssndChunkFound = true
                    val dataStartPosition = input.position + 8
                    val dataLength = (chunkSize - 8).toLong()
                    return Pair(dataStartPosition, dataLength)
                }
            }
            throw IOException("SSND chunk not found")
        }
    }


    class ChunkHeader(val form: String, val size: Int, val type: String) {
        companion object {
            @Throws(IOException::class)
            fun peek(input: ExtractorInput, scratch: ParsableByteArray): ChunkHeader {
                input.peekFully(scratch.data, 0, AIFC_HEADER_SIZE)
                scratch.position = 0
                val form = scratch.readString(4)
                val dataSize = scratch.readInt()
                val type = scratch.readString(4)

                return ChunkHeader(form, dataSize, type)
            }
        }
    }
}

const val AIFC_HEADER_SIZE = 12
