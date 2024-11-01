package org.jugregator.op1buddy.features.sync

import android.content.Context
import androidx.compose.runtime.toMutableStateList
import java.io.File

class LocalFileRepositoryImpl(private val context: Context) : LocalFileRepository {
    override fun readBackupInfo(backupDirPath: String): BackupInfo {
        val backupDir = File(context.filesDir, "op1backup/${backupDirPath}")
        // read structure
        // tape_1.aif .. tape_4.aif
        // synth_1.aif .. synth_8.aif
        // drum_1.aif .. drum_8.aif
        val backupFiles = backupDir.listFiles() ?: arrayOf()

        var backupInfo = BackupInfo(drumkitsEnabled = false, synthsEnabled = false)
        val disabledTapes =
            backupInfo.tapes.map { it.copy(first = it.first.copy(enabled = false)) }.toMutableStateList()
        backupInfo = backupInfo.copy(tapes = disabledTapes)

        var drumCount = 0
        var synthCount = 0
        for (file in backupFiles) {
            if (file.nameWithoutExtension.startsWith("tape_")) {
                val tapeIndexStr = file.nameWithoutExtension.split("_").last()

                val tapeIndex = try {
                    Integer.parseInt(tapeIndexStr)
                } catch (e: NumberFormatException) {
                    // Wrong file try next
                    continue
                } - 1

                val tapes = backupInfo.tapes
                var (tape, _) = tapes[tapeIndex]
                tape = tape.copy(enabled = true)
                tapes[tapeIndex] = (tape to false)
                backupInfo = backupInfo.copy(tapes = tapes)
            }

            if (file.nameWithoutExtension.startsWith("drum_")) {
                val drumIndexStr = file.nameWithoutExtension.split("_").last()
                try {
                    Integer.parseInt(drumIndexStr)
                } catch (e: NumberFormatException) {
                    // Wrong file try next
                    continue
                }
                drumCount += 1
            }

            if (drumCount == DRUMKITS_COUNT) {
                backupInfo = backupInfo.copy(drumkitsEnabled = true)
            }

            if (file.nameWithoutExtension.startsWith("synth_")) {
                val drumIndexStr = file.nameWithoutExtension.split("_").last()
                try {
                    Integer.parseInt(drumIndexStr)
                } catch (e: NumberFormatException) {
                    // Wrong file try next
                    continue
                }
                synthCount += 1
            }

            if (synthCount == SYNTHS_COUNT) {
                backupInfo = backupInfo.copy(synthsEnabled = true)
            }
        }

        return backupInfo
    }
}

private const val DRUMKITS_COUNT = 8
private const val SYNTHS_COUNT = 8