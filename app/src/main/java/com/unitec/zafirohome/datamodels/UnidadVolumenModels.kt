package com.unitec.zafirohome.datamodels

import com.google.gson.annotations.SerializedName

// --- UNIDADES DE VOLUMEN (Units) ---

data class UnidadVolumenDto(
    @SerializedName("unidad_id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("abreviacion") val abreviacion: String? // Tu HTML tiene este campo
)

data class UnidadVolumenRequest(
    @SerializedName("unidad_id") val id: Int? = null,
    val nombre: String,
    val abreviacion: String
)

data class DeleteUnidadRequest(
    @SerializedName("unidad_id") val id: Int
)