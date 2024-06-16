package org.jugregator.op1buddy.features.sync

import me.jahnen.libaums.core.fs.FileSystem
import me.jahnen.libaums.core.fs.UsbFile
import java.io.File

interface UsbFileRepository {
    fun copyFileFromUsb(sourceUsbFile: UsbFile, fs: FileSystem, targetFile: File, onProgress: (Long) -> Unit)
    fun copyMultipleFilesFromUsb(
        usbFiles: List<UsbFile>,
        fs: FileSystem,
        targetFiles: List<File>,
        onProgress: (Float) -> Unit
    )
}