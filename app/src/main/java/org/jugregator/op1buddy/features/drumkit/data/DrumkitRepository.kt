package org.jugregator.op1buddy.features.drumkit.data

import android.content.Context
import kotlinx.serialization.json.Json
import org.jugregator.op1buddy.data.drumkit.DrumkitInfo
import org.jugregator.op1buddy.data.drumkit.parseDrumKit
import java.io.File

class DrumkitRepository(private val context: Context, private val json: Json) {
    fun loadDrumKit(projectPath: String, fileName: String): DrumkitInfo? {
        val fileInputStream = File(context.filesDir, "op1backup/$projectPath/$fileName").inputStream()
        return parseDrumKit(
            fileInputStream = fileInputStream,
            withSamples = true,
            json = json
        )
    }
}