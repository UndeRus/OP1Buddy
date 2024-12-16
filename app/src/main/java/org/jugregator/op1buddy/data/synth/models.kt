package org.jugregator.op1buddy.data.synth

data class SynthInfo(
    val filename: String,
    val name: String,
    val sample: ByteArray?,
    val metadata: SynthMetadata,
    val synthEngine: SynthEngine,
)

sealed class SynthEngine {
    class Sampler: SynthEngine()
    class FM: SynthEngine()
    class Cluster: SynthEngine()
    class DrWave: SynthEngine()
    class Digital: SynthEngine()
    class String: SynthEngine()
    class Pulse: SynthEngine()
    class Phase: SynthEngine()
    class DSynth: SynthEngine()
    class Voltage: SynthEngine()
    class DNA: SynthEngine()
    class Iter: SynthEngine()
    class Undefined: SynthEngine()
}

fun synthTypeFromString(typeStr: String): SynthEngine {
    return when(typeStr) {
        "sampler" -> SynthEngine.Sampler()
        "fm" -> SynthEngine.FM()
        "cluster" -> SynthEngine.Cluster()
        "digital" -> SynthEngine.Digital()
        "dna" -> SynthEngine.DNA()
        "drwave" -> SynthEngine.DrWave()
        "dsynth" -> SynthEngine.DSynth()
        "iter" -> SynthEngine.Iter()
        "phase" -> SynthEngine.Phase()
        "pulse" -> SynthEngine.Pulse()
        "string" -> SynthEngine.String()
        "voltage" -> SynthEngine.Voltage()
        else -> SynthEngine.Undefined()
    }
}