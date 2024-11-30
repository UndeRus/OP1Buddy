package org.jugregator.op1buddy.features.project.data.aiff

import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.extractor.SeekMap
import androidx.media3.extractor.SeekMap.SeekPoints
import androidx.media3.extractor.SeekPoint

@UnstableApi
class AifcSeekMap(
    private val aiffFormat: AifcFormat,
    private val framesPerBlock: Int,
    dataStartPosition: Long,
    dataEndPosition: Long
) :
    SeekMap {

    private val firstBlockPosition: Long = dataStartPosition
    private val blockCount: Long = (dataEndPosition - dataStartPosition) / aiffFormat.blockSize
    private val durationUs: Long

    init {
        durationUs = blockIndexToTimeUs(blockCount)
    }

    override fun isSeekable(): Boolean {
        return true
    }

    override fun getDurationUs(): Long {
        return durationUs
    }

    override fun getSeekPoints(timeUs: Long): SeekPoints {

        // Calculate the containing block index, constraining to valid indices.
        var blockIndex: Long = (timeUs * aiffFormat.frameRateHz) / (C.MICROS_PER_SECOND * framesPerBlock)
        blockIndex = Util.constrainValue(blockIndex, 0, blockCount - 1)

        val seekPosition: Long = firstBlockPosition + (blockIndex * aiffFormat.blockSize)
        val seekTimeUs = blockIndexToTimeUs(blockIndex)
        val seekPoint = SeekPoint(seekTimeUs, seekPosition)
        if (seekTimeUs >= timeUs || blockIndex == blockCount - 1) {
            return SeekPoints(seekPoint)
        } else {
            val secondBlockIndex = blockIndex + 1
            val secondSeekPosition: Long = firstBlockPosition + (secondBlockIndex * aiffFormat.blockSize)
            val secondSeekTimeUs = blockIndexToTimeUs(secondBlockIndex)
            val secondSeekPoint = SeekPoint(secondSeekTimeUs, secondSeekPosition)
            return SeekPoints(seekPoint, secondSeekPoint)
        }
    }

    private fun blockIndexToTimeUs(blockIndex: Long): Long {
        return Util.scaleLargeTimestamp(
            blockIndex * framesPerBlock, C.MICROS_PER_SECOND, aiffFormat.frameRateHz.toLong()
        )
    }
}
