package com.unitec.zafirohome.datamodels

import com.google.gson.annotations.SerializedName

// --- MODELO UI / DTO (Lo que recibimos del GET) ---
data class MarcaDto(
    @SerializedName("marca_id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

// --- REQUESTS (Lo que enviamos en POST) ---

// Para Insertar y Actualizar
data class MarcaRequest(
    @SerializedName("marca_id") val id: Int? = null, // Null al insertar, Obligatorio al actualizar
    val nombre: String
)

// Para Eliminar: El backend espera { "marca_id": X }
data class DeleteMarcaRequest(
    @SerializedName("marca_id") val id: Int
)