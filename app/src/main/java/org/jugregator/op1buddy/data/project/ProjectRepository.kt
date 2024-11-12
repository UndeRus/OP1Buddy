package org.jugregator.op1buddy.data.project

import org.jugregator.op1buddy.data.drumkit.DrumkitInfo
import org.jugregator.op1buddy.data.synth.SynthInfo

interface ProjectRepository {
    fun readDrumKits(project: Project): List<DrumkitInfo>
    fun readSynths(project: Project): List<SynthInfo>
    fun readTapes(project: Project)
}