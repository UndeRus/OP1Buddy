package org.jugregator.op1buddy.data.tape

import org.jugregator.op1buddy.data.RegnChunk
import org.jugregator.op1buddy.data.readFully
import org.jugregator.op1buddy.data.readInt
import org.jugregator.op1buddy.features.project.data.aiff.hexArrayToDecimal
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun readTapeAiffMetadataDataAndroid(inputStream: InputStream): AiffTapeMetadata? {
    var regnChunk: RegnChunk? = null

    var cursor = 0L
    var startOffset = -1L
    var endOffset = -1L

    inputStream.let { input ->
        val buffer = ByteArray(4)
        input.readFully(buffer)
        cursor += 4

        // Check for "FORM" header at the beginning of the file
        if (buffer.decodeToString() != "FORM") {
            throw IllegalArgumentException("Not a valid AIFF file.")
        }

        // Skip file size (4 bytes) and verify "AIFF" header
        input.skip(4)
        cursor += 4

        input.readFully(buffer)
        val formatTag = buffer.decodeToString()
        if (!formatTag.contains("AIFC") && !formatTag.contains("AIFF")) {
            throw IllegalArgumentException("Not a valid AIFF file.")
        }

        // Start reading chunks
        while (input.available() > 0) {
            // Read chunk ID and chunk size
            input.readFully(buffer)
            cursor += 4
            val chunkId = buffer.decodeToString()
            val chunkSize = input.readInt()
            cursor += 4


            // Check if it's the APPL chunk
            when (chunkId) {
                "regn" -> {
                    // Read regn chunk data
                    val data = ByteArray(chunkSize)
                    input.readFully(data)
                    cursor += chunkSize
                    regnChunk = RegnChunk(id = chunkId, size = chunkSize, data = data)
                }

                "SSND" -> {
                    // Read SSND chunk
                    val offset = input.readInt()    // Sound data offset
                    cursor += 4
                    val blockSize = input.readInt() // Block size
                    cursor += 4
                    val soundData = ByteArray(chunkSize - 8) // Adjust for offset and blockSize fields
                    startOffset = cursor
                    //input.readFully(soundData)
                    input.skip((chunkSize - 8).toLong())
                    cursor += chunkSize - 8
                    endOffset = cursor
                }

                else -> {
                    // Skip other chunks
                    //println("Skip $chunkId")
                    input.skip(chunkSize.toLong())
                    cursor += chunkSize
                }
            }
            // Stop if both chunks are found
            if (regnChunk != null && startOffset != -1L && endOffset != -1L) {
                break
            }
        }
    }
    val localRegnChunk = regnChunk
    return if (startOffset != -1L && endOffset != -1L && localRegnChunk != null) {
        AiffTapeMetadata(startOffset = startOffset, endOffset = endOffset, regions = localRegnChunk.intoRegionData())
    } else {
        null
    }
}


data class AiffTapeMetadata(
    val startOffset: Long,
    val endOffset: Long,
    val regions: RegionData,
)

data class RegionData(val count: Int, val regions: List<Pair<Long, Long>>)

fun RegnChunk.intoRegionData(): RegionData {
    val stream = ByteArrayInputStream(data)

    val countBytes = ByteArray(4)
    stream.read(countBytes)

    val regionCount = ByteBuffer.wrap(countBytes).order(ByteOrder.BIG_ENDIAN).getInt()

    val regions = mutableListOf<Pair<Long, Long>>()

    val regionBuffer = ByteArray(88)

    for (i in 1..regionCount) {
        stream.read(regionBuffer, 0, regionBuffer.size)

        val start = regionBuffer.sliceArray(20..23)
        val end = regionBuffer.sliceArray(28..31)

        val startSample = hexArrayToDecimal(start)
        val endSample = hexArrayToDecimal(end)

        regions.add(startSample.toLong() to endSample.toLong())
    }

    regions.sortBy { it.first }


    return RegionData(count = regionCount, regions = regions)
}
