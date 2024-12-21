package org.jugregator.op1buddy.data.drumkit

import kotlinx.serialization.json.Json
import org.jugregator.op1buddy.data.ApplChunk
import org.jugregator.op1buddy.data.SsndChunk
import org.jugregator.op1buddy.data.readFully
import org.jugregator.op1buddy.data.readInt
import java.io.File
import java.io.InputStream

@Throws(IllegalArgumentException::class)
fun readDrumKitAiffDataAndroid(inputStream: InputStream): Pair<ApplChunk?, SsndChunk?> {
    var applChunk: ApplChunk? = null
    var ssndChunk: SsndChunk? = null
    inputStream.use { input ->
        val buffer = ByteArray(INT_SIZE)

        // Check for "FORM" header at the beginning of the file
        input.readFully(buffer)

        require(buffer.decodeToString() == "FORM") {
            "Not a valid AIFF/AIFC file."
        }

        // Read the size of the FORM chunk (4 bytes) and skip it
        input.skip(INT_SIZE.toLong())

        // Verify "AIFF" or "AIFC" format identifier
        input.readFully(buffer)
        val formatTag = buffer.decodeToString()
        require(formatTag.contains("AIFC") || formatTag.contains("AIFF")) {
            "Not a valid AIFF or AIFC file."
        }

        // Start reading chunks after the FORM header
        while (input.available() > 0) {
            // Read chunk ID and chunk size
            input.readFully(buffer)
            val chunkId = buffer.decodeToString()
            val chunkSize = input.readInt()

            when (chunkId) {
                "APPL" -> {
                    // Read APPL chunk data
                    val data = ByteArray(chunkSize)
                    input.readFully(data)
                    applChunk = ApplChunk(id = chunkId, size = chunkSize, data = data)
                }

                "SSND" -> {
                    // Read SSND chunk
                    val offset = input.readInt()    // Sound data offset
                    val blockSize = input.readInt() // Block size
                    val soundData = ByteArray(chunkSize - 8) // Adjust for offset and blockSize fields
                    input.readFully(soundData)
                    ssndChunk = SsndChunk(
                        id = chunkId,
                        size = chunkSize,
                        offset = offset,
                        blockSize = blockSize,
                        soundData = soundData
                    )
                }

                else -> {
                    // Skip other chunks
                    input.skip(chunkSize.toLong())
                }
            }

            // Stop if both chunks are found
            if (applChunk != null && ssndChunk != null) break
        }
    }

    return Pair(applChunk, ssndChunk)
}

fun parseDrumKitFromFile(filePath: String, json: Json): DrumkitInfo? {
    val file = File(filePath)
    return parseDrumKit(filePath = filePath, fileInputStream = file.inputStream(), json = json)
}

fun parseDrumKit(
    filePath: String = "",
    fileInputStream: InputStream,
    withSamples: Boolean = false,
    json: Json
): DrumkitInfo? {
    val (applChunk, ssndChunk) = readDrumKitAiffDataAndroid(fileInputStream)

    if (applChunk != null) {
        var drumkitData = applChunk.data.decodeToString()
        drumkitData = drumkitData.drop(4) // remove op-1 header
        val data: DrumKitMetadata = json.decodeFromString(drumkitData)
        if (ssndChunk != null && withSamples) {
            val chunks = mutableListOf<ByteArray>()
            for (record in data.start.zip(data.end).withIndex()) {
                val (index, startEnd) = record
                val (start, end) = startEnd
                val byteStart = start / SAMPLE_CONVERSION
                val byteEnd = end / SAMPLE_CONVERSION
                //println("$index Start-end $byteStart $byteEnd")
                val currentChunk = ssndChunk.soundData.copyOfRange(byteStart * 2, byteEnd * 2)
                chunks.add(currentChunk)
            }
            return DrumkitInfo(
                filename = filePath,
                name = data.name ?: "",
                samples = chunks,
                metadata = data,
                drumType = data.getType()
            )
        } else {
            return DrumkitInfo(
                filename = filePath,
                name = data.name ?: "",
                samples = listOf(),
                metadata = data,
                drumType = data.getType()
            )
        }
    } else {
        println("APPL Chunk not found.")
    }
    return null
}

private const val INT_SIZE: Int = 4
private const val SAMPLE_CONVERSION = 4058
