package com.unitec.zafirohome.datamodels

import com.google.gson.annotations.SerializedName

// --- TALLAS (Sizes) ---

data class TallaDto(
    @SerializedName("talla_id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

data class TallaRequest(
    @SerializedName("talla_id") val id: Int? = null,
    val nombre: String
)

data class DeleteTallaRequest(
    @SerializedName("talla_id") val id: Int
)