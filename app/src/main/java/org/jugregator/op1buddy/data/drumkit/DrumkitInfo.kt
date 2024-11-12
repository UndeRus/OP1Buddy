package org.jugregator.op1buddy.data.drumkit

data class DrumkitInfo(
    val filename: String,
    val name: String,
    val samples: List<ByteArray>,
    val metadata: DrumKitMetadata,
)