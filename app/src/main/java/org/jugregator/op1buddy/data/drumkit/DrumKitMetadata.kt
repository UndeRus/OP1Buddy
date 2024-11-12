package org.jugregator.op1buddy.data.drumkit

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DrumKitMetadata(
    @SerialName("adsr") var adsr: ArrayList<Int> = arrayListOf(),
    @SerialName("drum_version") var drumVersion: Int? = null,
    @SerialName("dyna_env") var dynaEnv: ArrayList<Int> = arrayListOf(),
    @SerialName("end") var end: ArrayList<Int> = arrayListOf(),
    @SerialName("fx_active") var fxActive: Boolean? = null,
    @SerialName("fx_params") var fxParams: ArrayList<Int> = arrayListOf(),
    @SerialName("fx_type") var fxType: String? = null,
    @SerialName("lfo_active") var lfoActive: Boolean? = null,
    @SerialName("lfo_params") var lfoParams: ArrayList<Int> = arrayListOf(),
    @SerialName("lfo_type") var lfoType: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("octave") var octave: Int? = null,
    @SerialName("pitch") var pitch: ArrayList<Int> = arrayListOf(),
    @SerialName("playmode") var playmode: ArrayList<Int> = arrayListOf(),
    @SerialName("reverse") var reverse: ArrayList<Int> = arrayListOf(),
    @SerialName("start") var start: ArrayList<Int> = arrayListOf(),
    @SerialName("type") var type: String? = null,
    @SerialName("volume") var volume: ArrayList<Int> = arrayListOf()

)