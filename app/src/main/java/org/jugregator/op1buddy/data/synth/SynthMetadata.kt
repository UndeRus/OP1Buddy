package org.jugregator.op1buddy.data.synth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SynthMetadata(
    @SerialName("adsr") var adsr: ArrayList<Int> = arrayListOf(),
    @SerialName("base_freq") var baseFreq: Double? = null,
    @SerialName("fx_active") var fxActive: Boolean? = null,
    @SerialName("fx_params") var fxParams: ArrayList<Int> = arrayListOf(),
    @SerialName("fx_type") var fxType: String? = null,
    @SerialName("knobs") var knobs: ArrayList<Int> = arrayListOf(),
    @SerialName("lfo_active") var lfoActive: Boolean? = null,
    @SerialName("lfo_params") var lfoParams: ArrayList<Int> = arrayListOf(),
    @SerialName("lfo_type") var lfoType: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("octave") var octave: Int? = null,
    @SerialName("synth_version") var synthVersion: Int? = null,
    @SerialName("type") var type: String? = null
)