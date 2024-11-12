package org.jugregator.op1buddy.data.project

import org.jugregator.op1buddy.features.sync.BackupInfo

interface LocalFileRepository {
    fun readBackupInfo(backupDirPath: String): BackupInfo
}
