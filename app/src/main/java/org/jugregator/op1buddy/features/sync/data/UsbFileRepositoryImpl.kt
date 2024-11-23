package org.jugregator.op1buddy.features.sync.data

import me.jahnen.libaums.core.fs.FileSystem
import me.jahnen.libaums.core.fs.UsbFile
import me.jahnen.libaums.core.fs.UsbFileStreamFactory
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import kotlin.jvm.Throws

class UsbFileRepositoryImpl : UsbFileRepository {
    @Throws(IOException::class)
    override fun copyFileFromUsb(sourceUsbFile: UsbFile, fs: FileSystem, targetFile: File, onProgress: (Long) -> Unit) {
        val outputStream = BufferedOutputStream(targetFile.outputStream()) // IO
        val input = UsbFileStreamFactory.createBufferedInputStream(sourceUsbFile, fs) // singleThread
        val buffer = ByteArray(fs.chunkSize)
        var fullSize = 0L
        var resultSize = input.read(buffer) // singleThread

        while (resultSize != -1) {
            outputStream.write(buffer) // IO
            fullSize += resultSize
            onProgress(resultSize.toLong())
            resultSize = input.read(buffer) // singleThread
        }
        outputStream.close() // IO

        //println("$sourceUsbFile to $targetFile copied")
    }

    @Throws(IOException::class, IllegalStateException::class)
    override fun copyMultipleFilesFromUsb(
        usbFiles: List<UsbFile>,
        fs: FileSystem,
        targetFiles: List<File>,
        onProgress: (Float) -> Unit
    ) {
        if (usbFiles.size != targetFiles.size) {
            error("usbFiles and targetFiles must be same length")
        }

        val fullLength = usbFiles.sumOf { it.length }
        var currentLength = 0L
        for ((usbFile, file) in usbFiles.zip(targetFiles)) {
            copyFileFromUsb(usbFile, fs, file) { progress ->
                currentLength += progress
                onProgress((currentLength.toFloat() / fullLength.toFloat()))
            }
        }
    }

    override fun copyFileToUsb(targetUsbFile: UsbFile, fs: FileSystem, sourceFile: File, onProgress: (Long) -> Unit) {
        val fileSize = sourceFile.length()
        val outputStream = UsbFileStreamFactory.createBufferedOutputStream(targetUsbFile, fs)
        val input = BufferedInputStream(sourceFile.inputStream())

        val buffer = ByteArray(fs.chunkSize)
        var fullSize = 0L
        var resultSize = input.read(buffer)

        while (resultSize != -1) {
            outputStream.write(buffer, 0, resultSize)
            fullSize += resultSize
            onProgress(resultSize.toLong())
            resultSize = input.read(buffer)
        }
        outputStream.close()
        input.close()
    }

    override fun copyMultipleFilesToUsb(
        targetUsbFiles: List<UsbFile>,
        fs: FileSystem,
        sourceFiles: List<File>,
        onProgress: (Float) -> Unit
    ) {
        if (targetUsbFiles.size != sourceFiles.size) {
            error("targetUsbFiles anmd sourceFiles must be same length")
        }

        val fullLength = sourceFiles.sumOf { it.length() }
        var currentLength = 0L
        for ((file, usbFile) in sourceFiles.zip(targetUsbFiles)) {
            copyFileToUsb(usbFile, fs, file) { progress ->
                currentLength += progress
                onProgress((currentLength.toFloat() / fullLength.toFloat()))
            }
        }
    }
}
