package org.jugregator.op1buddy.data.project

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class BackupRepositoryImpl : BackupRepository {
    override fun compressBackup(filesDir: File, outputZipFileOutputStream: OutputStream) {
        try {
            val files = (filesDir.listFiles() ?: arrayOf<File>()).map { it.name }

            var origin: BufferedInputStream?

            val out = ZipOutputStream(BufferedOutputStream(outputZipFileOutputStream))

            val data = ByteArray(BUFFER_SIZE)
            for (filename in files) {
                val file = File(filesDir, filename)
                val fi = FileInputStream(file)
                origin = BufferedInputStream(fi, BUFFER_SIZE)

                val entry = ZipEntry(filename.substring(filename.lastIndexOf("/") + 1))
                out.putNextEntry(entry)

                var count = origin.read(data, 0, BUFFER_SIZE)

                while (count != -1) {
                    out.write(data, 0, count)
                    count = origin.read(data, 0, BUFFER_SIZE)
                }
                origin.close()
            }
            out.close()
        } catch (e: IOException) {
            Firebase.crashlytics.log("IOException during export backup")
            Firebase.crashlytics.recordException(e)
        } catch (e: ZipException) {
            Firebase.crashlytics.log("ZipException during export backup")
            Firebase.crashlytics.recordException(e)
        }
    }

    override fun importBackup(projectDir: File, inputZipFileInputStream: () -> InputStream?) {
        try {
            var input = ZipInputStream(BufferedInputStream(inputZipFileInputStream() ?: return))

            val entries = sequence {
                var zipEntry = input.getNextEntry()
                while (zipEntry != null) {
                    yield(zipEntry)
                    zipEntry = input.getNextEntry()
                }
            }

            val backupInfo = readBackupInfo({ entries }, { it.name })

            input = ZipInputStream(BufferedInputStream(inputZipFileInputStream() ?: return))

            var zipEntry = input.nextEntry
            while (zipEntry != null) {
                if (zipEntry.name.startsWith("synth_") && backupInfo.synthsEnabled) {
                    copyFileFromBackup(projectDir, zipEntry, input)
                }
                if (zipEntry.name.startsWith("drum_") && backupInfo.drumkitsEnabled) {
                    copyFileFromBackup(projectDir, zipEntry, input)
                }

                if (zipEntry.name.startsWith("tape_")) {
                    copyFileFromBackup(projectDir, zipEntry, input)
                }

                zipEntry = input.nextEntry
            }

        } catch (e: IOException) {
            Firebase.crashlytics.log("IOException during import backup")
            Firebase.crashlytics.recordException(e)
        } catch (e: ZipException) {
            Firebase.crashlytics.log("ZipException during import backup")
            Firebase.crashlytics.recordException(e)
        }
    }

    private fun copyFileFromBackup(projectDir: File, zipEntry: ZipEntry, input: ZipInputStream) {
        val outputFile = File(projectDir, zipEntry.name)
        if (!outputFile.exists()) {
            outputFile.createNewFile()
        }
        val bufferedInputStream = BufferedInputStream(input)
        val outWriter = FileOutputStream(outputFile)

        val buffer = ByteBuffer.allocate(BUFFER_SIZE).array()
        do {
            val read = bufferedInputStream.read(buffer)
            outWriter.write(buffer)
        } while (
            read == BUFFER_SIZE
        )
        input.closeEntry()
        outWriter.close()
    }
}

private const val BUFFER_SIZE = 512
