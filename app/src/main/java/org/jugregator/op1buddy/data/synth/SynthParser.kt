package org.jugregator.op1buddy.data.synth

import kotlinx.serialization.json.Json
import org.jugregator.op1buddy.data.ApplChunk
import org.jugregator.op1buddy.data.SsndChunk
import org.jugregator.op1buddy.data.readFully
import org.jugregator.op1buddy.data.readInt
import java.io.File
import java.io.InputStream

fun readSynthAiffDataAndroid(inputStream: InputStream): Pair<ApplChunk?, SsndChunk?> {
    var applChunk: ApplChunk? = null
    var ssndChunk: SsndChunk? = null

    inputStream.use { input ->
        val buffer = ByteArray(4)

        // Check for "FORM" header at the beginning of the file
        input.readFully(buffer)

        require(buffer.decodeToString() == "FORM") {
            "Not a valid AIFF/AIFC file."
        }

        // Read the size of the FORM chunk (4 bytes) and skip it
        input.skip(4)

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
                    //println("Skip TAG $chunkId")
                    input.skip(chunkSize.toLong())
                }
            }

            // Stop if both chunks are found
            if (applChunk != null && ssndChunk != null) break
        }
    }

    return Pair(applChunk, ssndChunk)
}

fun readSynthAiffDataAndroid(filePath: String): Pair<ApplChunk?, SsndChunk?> {
    val file = File(filePath)
    return readSynthAiffDataAndroid(file.inputStream())
}

fun parseSynth(filePath: String, json: Json): SynthInfo? {
    val file = File(filePath)
    return parseSynth(filePath, file.inputStream(), json = json)
}

fun parseSynth(filePath: String = "", inputStream: InputStream, json: Json): SynthInfo? {
    val (applChunk, ssndChunk) = readSynthAiffDataAndroid(inputStream)

    if (applChunk != null) {
        var drumkitData = applChunk.data.decodeToString()
        drumkitData = drumkitData.drop(4) // remove op-1 header
        val data: SynthMetadata = json.decodeFromString(drumkitData)
        if (ssndChunk != null) {
            return SynthInfo(
                filename = filePath,
                name = data.name ?: "",
                sample = ssndChunk.soundData,
                synthEngine = synthTypeFromString(data.type ?: ""),
                metadata = data,
            )
        }
    } else {
        println("APPL Chunk not found.")
    }
    return null
}