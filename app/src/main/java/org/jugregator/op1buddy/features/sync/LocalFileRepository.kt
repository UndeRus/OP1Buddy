package org.jugregator.op1buddy.features.sync

interface LocalFileRepository {
    fun readBackupInfo(backupDirPath: String): BackupInfo
}
