package org.jugregator.op1buddy.data.project

import java.io.File
import java.io.InputStream
import java.io.OutputStream

interface BackupRepository {
    fun compressBackup(filesDir: File, outputZipFileOutputStream: OutputStream)

    fun importBackup(projectDir: File, inputZipFileInputStream: () -> InputStream?)
}
