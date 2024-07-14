package org.jugregator.op1buddy.features.sync

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipOutputStream

class BackupRepositoryImpl : BackupRepository {
    override fun compressBackup(filesDir: File, outputZipFileOutputStream: OutputStream) {
        try {
            val files = (filesDir.listFiles() ?: arrayOf<File>()).map { it.name }

            var origin: BufferedInputStream? = null

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
            e.printStackTrace()
        } catch (e: ZipException) {
            e.printStackTrace()
        }
    }
}

private const val BUFFER_SIZE = 512