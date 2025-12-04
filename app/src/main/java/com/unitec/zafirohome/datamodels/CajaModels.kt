package com.unitec.zafirohome.datamodels

import com.google.gson.annotations.SerializedName

// --- DTO para recibir datos ---
data class CajaDto(
    @SerializedName("caja_id") val id: Int,
    @SerializedName("letra") val letra: String,
    @SerializedName("cara") val cara: Int,
    @SerializedName("nivel") val nivel: Int
)

// --- Request para Insertar/Actualizar ---
data class CajaRequest(
    @SerializedName("caja_id") val id: Int? = null,
    val letra: String,
    val cara: Int,
    val nivel: Int
)

