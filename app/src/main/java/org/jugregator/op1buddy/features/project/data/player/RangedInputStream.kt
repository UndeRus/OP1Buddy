package org.jugregator.op1buddy.features.project.data.player

import java.io.IOException
import java.io.InputStream

class RangedInputStream(
    private val startOffset: Long,
    private val endOffset: Long,
    private val innerInputStream: InputStream,
): InputStream() {
    private var position: Long = 0L

    init {
        reset()
    }

    @Throws(IOException::class)
    override fun reset() {
        innerInputStream.reset()
        if (innerInputStream.skip(startOffset) != startOffset) {
            throw IOException("Unable to skip to start position")
        }
        position = startOffset
    }

    override fun markSupported(): Boolean {
        return false
    }

    @Throws(IOException::class)
    override fun read(): Int {
        if (position >= endOffset) {
            return -1 // End of range
        }
        val data: Int = innerInputStream.read()
        if (data != -1) {
            position++
        }
        return data
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray?, off: Int, len: Int): Int {
        if (position >= endOffset) {
            return -1 // End of range
        }
        val realLength = Math.min(len.toLong(), endOffset - position).toInt()

        val bytesRead: Int = innerInputStream.read(b, off, realLength)
        if (bytesRead != -1) {
            position += bytesRead
        }
        return bytesRead
    }

    @Throws(IOException::class)
    override fun close() {
        innerInputStream.close()
    }
}
