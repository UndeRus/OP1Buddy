package org.jugregator.op1buddy.data


data class ApplChunk(val id: String, val size: Int, val data: ByteArray)
data class RegnChunk(val id: String, val size: Int, val data: ByteArray)
data class SsndChunk(val id: String, val size: Int, val offset: Int, val blockSize: Int, val soundData: ByteArray)
