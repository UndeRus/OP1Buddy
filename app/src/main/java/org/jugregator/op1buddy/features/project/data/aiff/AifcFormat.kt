package org.jugregator.op1buddy.features.project.data.aiff

data class AifcFormat(
    val numChannels: Int = 1,
    val frameRateHz: Int = 44100,
    val bitsPersample: Int = 16,
    val blockSize: Int = 2,
)
