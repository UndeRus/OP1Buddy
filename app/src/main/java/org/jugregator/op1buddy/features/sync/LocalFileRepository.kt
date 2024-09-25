package org.jugregator.op1buddy.features.sync

import java.io.File

interface LocalFileRepository {
    fun readBackupInfo(backupDir: File): BackupInfo
}
