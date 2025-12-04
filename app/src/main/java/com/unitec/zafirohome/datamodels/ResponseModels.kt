package com.unitec.zafirohome.datamodels

import com.google.gson.annotations.SerializedName

// --- 1. WRAPPER GENÉRICO PARA LISTAS (GET) ---
data class ResponseWithData<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<T>? = null
)

// --- 2. WRAPPER GENÉRICO PARA ACCIONES (POST/PUT/DELETE) ---
data class EmptyResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)

// --- 3. RESPUESTA ESPECÍFICA PARA SUBIDA DE IMÁGENES ---
// Separada de las otras dos para manejar lógica específica de archivos si es necesario
data class ImageResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    // Campo opcional por si el servidor devuelve la URL de la imagen subida
    @SerializedName("url")
    val url: String? = null
)