package com.unitec.zafirohome.datamodels

import com.google.gson.annotations.SerializedName

// Ajusta esta IP si tu servidor cambia de dirección
private const val BASE_URL = "http://31.97.150.130:3000"

// --- DTO DE LECTURA (Lo que llega en get_all) ---
data class ProductoDto(
    @SerializedName("producto_id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio_base") val precio: Double,
    @SerializedName("stock") val stock: Int,

    // 1. CAMPO DE IMAGEN (Puede ser null)
    @SerializedName("imagen") val imagen: String?,

    // IDs de relaciones
    @SerializedName("marca_id") val marcaId: Int?,
    @SerializedName("categoria_id") val categoriaId: Int?,
    @SerializedName("categoria_secundaria_id") val categoriaSecId: Int?,
    @SerializedName("subcategoria_id") val subcategoriaId: Int?,
    @SerializedName("talla_id") val tallaId: Int?,
    @SerializedName("unidad_id") val unidadId: Int?,
    @SerializedName("caja_id") val cajaId: Int?,

    // Nombres de relaciones
    @SerializedName("marca_nombre") val marcaNombre: String?,
    @SerializedName("categoria_nombre") val categoriaNombre: String?
) {
    // 2. PROPIEDAD CALCULADA (Esto corrige el error 'Unresolved reference')
    val imagenUrlCompleta: String?
        get() {
            if (imagen.isNullOrBlank()) return null
            // Si la ruta ya trae http, la usamos. Si es relativa (/uploads/...), le pegamos la IP.
            return if (imagen.startsWith("http")) imagen else "$BASE_URL$imagen"
        }
}

// --- REQUEST DE ESCRITURA (Insert/Update) ---
data class ProductoRequest(
    @SerializedName("producto_id") val id: Int? = null,
    val nombre: String,
    val descripcion: String?,
    @SerializedName("precio_base") val precio: Double,
    val stock: Int,
    // Opcional: para enviar URL de imagen si fuera texto
    val imagen: String? = null,

    @SerializedName("marca_id") val marcaId: Int?,
    @SerializedName("categoria_id") val categoriaId: Int?,
    @SerializedName("categoria_secundaria_id") val categoriaSecId: Int?,
    @SerializedName("subcategoria_id") val subcategoriaId: Int?,
    @SerializedName("talla_id") val tallaId: Int?,
    @SerializedName("unidad_id") val unidadId: Int?,
    @SerializedName("caja_id") val cajaId: Int?
)

data class DeleteProductoRequest(
    @SerializedName("producto_id") val id: Int
)