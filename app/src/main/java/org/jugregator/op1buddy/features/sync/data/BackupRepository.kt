package org.jugregator.op1buddy.features.sync.data

import java.io.File
import java.io.OutputStream

interface BackupRepository {
    fun compressBackup(filesDir: File, outputZipFileOutputStream: OutputStream)
}