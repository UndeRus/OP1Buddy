package org.jugregator.op1buddy.features.sync.data

import me.jahnen.libaums.core.fs.FileSystem
import me.jahnen.libaums.core.fs.UsbFile
import java.io.File

interface UsbFileRepository {
    fun copyFileFromUsb(
        sourceUsbFile: UsbFile,
        fs: FileSystem,
        targetFile: File,
        onProgress: (Long) -> Unit,
    )

    fun copyMultipleFilesFromUsb(
        usbFiles: List<UsbFile>,
        fs: FileSystem,
        targetFiles: List<File>,
        onProgress: (Float) -> Unit,
    )

    fun copyFileToUsb(
        targetUsbFile: UsbFile,
        fs: FileSystem,
        sourceFile: File,
        onProgress: (Long) -> Unit,
    )

    fun copyMultipleFilesToUsb(
        targetUsbFiles: List<UsbFile>,
        fs: FileSystem,
        sourceFiles: List<File>,
        onProgress: (Float) -> Unit
    )
}