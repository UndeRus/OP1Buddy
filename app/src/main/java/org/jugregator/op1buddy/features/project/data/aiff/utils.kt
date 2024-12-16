package org.jugregator.op1buddy.features.project.data.aiff

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun hexArrayToDecimal2(array: ByteArray): Int {
    val byte0 = array[0].toInt()
    val byte1 = array[1].toInt()
    val result = (byte0 shl 8) + byte1
    return result
}

fun hexArrayToDecimal3(array: ByteArray): Int {
    val byte0 = array[0].toInt()
    val byte1 = array[1].toInt()
    val byte3 = array[2].toInt()
    val result = (byte0 shl 16) + (byte1 shl 8) + byte3
    return result
}

fun hexArrayToDecimal(array: ByteArray): Int {
    return ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).getInt()
}