package org.jugregator.op1buddy.data.project

import android.content.Context
import kotlinx.serialization.json.Json
import org.jugregator.op1buddy.data.drumkit.DrumkitInfo
import org.jugregator.op1buddy.data.drumkit.parseDrumKitFromFile
import org.jugregator.op1buddy.data.synth.SynthInfo
import org.jugregator.op1buddy.data.synth.parseSynth
import java.io.File

class ProjectRepositoryImpl(
    private val context: Context,
    private val json: Json,
) : ProjectRepository {
    override fun readDrumKits(project: Project): List<DrumkitInfo> {
        val projectDir = project.backupDir
        val result = mutableListOf<DrumkitInfo>()
        for (i in 1..8) {
            val file = File(context.filesDir, "op1backup/$projectDir/drum_$i.aif")
            parseDrumKitFromFile(
                filePath = file.absolutePath,
                json = json
            )?.also {
                result.add(it)
            }
        }
        return result
    }

    override fun readSynths(project: Project): List<SynthInfo> {
        val projectDir = project.backupDir
        val result = mutableListOf<SynthInfo>()
        for (i in 1..8) {
            val file = File(context.filesDir, "op1backup/$projectDir/synth_$i.aif")
            parseSynth(filePath = file.absolutePath, json = json)?.also {
                result.add(it)
            }
        }
        return result
    }

    override fun readTapes(project: Project) {
        TODO("Not yet implemented")
    }
}