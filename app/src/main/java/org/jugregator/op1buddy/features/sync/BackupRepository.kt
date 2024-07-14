package org.jugregator.op1buddy.features.sync

import java.io.File
import java.io.OutputStream

interface BackupRepository {
    fun compressBackup(filesDir: File, outputZipFileOutputStream: OutputStream)
}