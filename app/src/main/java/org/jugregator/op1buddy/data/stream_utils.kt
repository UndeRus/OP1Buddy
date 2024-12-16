package org.jugregator.op1buddy.data

import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun FileInputStream.readFully(buffer: ByteArray) {
    var bytesRead: Int
    var offset = 0
    while (offset < buffer.size) {
        bytesRead = read(buffer, offset, buffer.size - offset)
        if (bytesRead == -1) throw IOException("Unexpected end of file")
        offset += bytesRead
    }
}

fun FileInputStream.readInt(): Int {
    val buffer = ByteArray(4)
    readFully(buffer)
    return ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN).int
}

fun InputStream.readFully(buffer: ByteArray) {
    var bytesRead: Int
    var offset = 0
    while (offset < buffer.size) {
        bytesRead = read(buffer, offset, buffer.size - offset)
        if (bytesRead == -1) throw IOException("Unexpected end of file")
        offset += bytesRead
    }
}

fun InputStream.readInt(): Int {
    val buffer = ByteArray(4)
    readFully(buffer)
    return ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN).int
}

