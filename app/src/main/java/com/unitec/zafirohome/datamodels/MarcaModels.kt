package com.unitec.zafirohome.datamodels

import com.google.gson.annotations.SerializedName

// --- MODELO UI / DTO (Lo que recibimos del GET) ---
data class MarcaDto(
    // El servidor envía "brand_id"
    @SerializedName("brand_id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

// --- REQUESTS (Lo que enviamos en POST) ---

// Para Insertar y Actualizar
data class MarcaRequest(
    // Asumimos que el servidor también espera "brand_id" para actualizar
    @SerializedName("brand_id") val id: Int? = null,
    val nombre: String
)

// Para Eliminar: El backend espera { "brand_id": X }
data class DeleteMarcaRequest(
    @SerializedName("brand_id") val id: Int
)