package org.jugregator.op1buddy.features.drumkit.data

import org.jugregator.op1buddy.data.drumkit.DrumkitInfo

interface DrumkitRepository {
    fun loadDrumKit(projectPath: String, fileName: String): DrumkitInfo?
}
