package com.unitec.zafirohome.datamodels

import com.google.gson.annotations.SerializedName

// --- ENUMS PARA MANEJAR LOS NIVELES ---
enum class NivelCategoria {
    PRINCIPAL, SECUNDARIA, SUBCATEGORIA
}

// NOTE: CategoriasResponse removed (replaced by ResponseWithData in ResponseModels.kt)

// --- MODELO UNIFICADO PARA LA UI (Lo que usa la LazyColumn) ---
data class CategoriaUiItem(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null, // Solo para Principal
    val tipo: NivelCategoria
)

// --- DTOs (Data Transfer Objects) PARA RETROFIT ---
// Sirven para mapear la respuesta exacta del servidor

data class CategoriaPrincipalDto(
    @SerializedName("categoria_id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String?
)

data class CategoriaSecundariaDto(
    @SerializedName("categoria_secundaria_id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

data class SubcategoriaDto(
    @SerializedName("subcategoria_id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

// --- REQUESTS PARA INSERTAR/ACTUALIZAR/BORRAR ---
// El backend pide nombres de campos específicos para cada nivel

data class CategoriaRequest(
    val id: Int? = null, // Opcional para insert
    val nombre: String,
    val descripcion: String? = null
)

// Para borrar, el backend espera un JSON { "categoria_id": 5 }
data class DeletePrincipalRequest(@SerializedName("categoria_id") val id: Int)
data class DeleteSecundariaRequest(@SerializedName("categoria_secundaria_id") val id: Int)
data class DeleteSubRequest(@SerializedName("subcategoria_id") val id: Int)