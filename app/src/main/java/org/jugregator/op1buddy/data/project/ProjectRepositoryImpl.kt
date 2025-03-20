package org.jugregator.op1buddy.data.project

import android.content.Context
import kotlinx.serialization.json.Json
import org.jugregator.op1buddy.data.drumkit.DrumkitInfo
import org.jugregator.op1buddy.data.drumkit.parseDrumKitFromFile
import org.jugregator.op1buddy.data.synth.SynthInfo
import org.jugregator.op1buddy.data.synth.parseSynth
import org.jugregator.op1buddy.data.tape.AiffTapeMetadata
import org.jugregator.op1buddy.data.tape.readTapeAiffMetadataDataAndroid
import org.jugregator.op1buddy.features.project.data.player.OptimizedRandomAccessFile
import org.jugregator.op1buddy.features.project.data.player.RangedRandomAccessFileInputStream
import java.io.File
import java.io.InputStream

class ProjectRepositoryImpl(
    private val context: Context,
    private val json: Json,
) : ProjectRepository {
    override fun readDrumKits(project: Project): List<DrumkitInfo> {
        val projectDir = project.backupDir
        val result = mutableListOf<DrumkitInfo>()
        for (i in 1..8) {
            val file = File(context.filesDir, "op1backup/$projectDir/drum_$i.aif")
            if (file.exists()) {
                parseDrumKitFromFile(
                    filePath = file.absolutePath,
                    json = json
                )?.also {
                    result.add(it)
                }
            }
        }
        return result
    }

    override fun readSynths(project: Project): List<SynthInfo> {
        val projectDir = project.backupDir
        val result = mutableListOf<SynthInfo>()
        for (i in 1..8) {
            val file = File(context.filesDir, "op1backup/$projectDir/synth_$i.aif")
            if (file.exists()) {
                parseSynth(filePath = file.absolutePath, json = json)?.also {
                    result.add(it)
                }
            }
        }
        return result
    }

    override fun readTapes(project: Project): List<Pair<InputStream, AiffTapeMetadata>> {
        val projectDir = project.backupDir
        val tapes = listOf(
            "tape_1.aif",
            "tape_2.aif",
            "tape_3.aif",
            "tape_4.aif",
        )
        return tapes.mapNotNull { tapeName ->
            val file = File(context.filesDir, "op1backup/$projectDir/$tapeName")
            if (file.exists()) {
                val raf = OptimizedRandomAccessFile(file, "r")
                val inputStream = file.inputStream()

                val metadata = readTapeAiffMetadataDataAndroid(inputStream)

                metadata?.let {
                    val sampleInputStream =
                        RangedRandomAccessFileInputStream(metadata.startOffset, metadata.endOffset, raf)
                    sampleInputStream to it
                }
            } else {
                null
            }
        }
    }
}
