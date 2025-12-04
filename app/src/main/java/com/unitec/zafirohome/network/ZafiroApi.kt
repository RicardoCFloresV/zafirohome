package com.unitec.zafirohome.network

import com.unitec.zafirohome.datamodels.CajaDto
import com.unitec.zafirohome.datamodels.CajaRequest
import com.unitec.zafirohome.datamodels.CategoriaPrincipalDto
import com.unitec.zafirohome.datamodels.CategoriaRequest
import com.unitec.zafirohome.datamodels.CategoriaSecundariaDto
import com.unitec.zafirohome.datamodels.DeleteMarcaRequest
import com.unitec.zafirohome.datamodels.DeletePrincipalRequest
import com.unitec.zafirohome.datamodels.DeleteProductoRequest
import com.unitec.zafirohome.datamodels.DeleteSecundariaRequest
import com.unitec.zafirohome.datamodels.DeleteSubRequest
import com.unitec.zafirohome.datamodels.DeleteTallaRequest
import com.unitec.zafirohome.datamodels.DeleteUnidadRequest
import com.unitec.zafirohome.datamodels.EmptyResponse
import com.unitec.zafirohome.datamodels.ImageResponse
import com.unitec.zafirohome.datamodels.LoginRequest
import com.unitec.zafirohome.datamodels.LoginResponse
import com.unitec.zafirohome.datamodels.MarcaDto
import com.unitec.zafirohome.datamodels.MarcaRequest
import com.unitec.zafirohome.datamodels.ProductoDto
import com.unitec.zafirohome.datamodels.ProductoRequest
import com.unitec.zafirohome.datamodels.ResponseWithData
import com.unitec.zafirohome.datamodels.SubcategoriaDto
import com.unitec.zafirohome.datamodels.TallaDto
import com.unitec.zafirohome.datamodels.TallaRequest
import com.unitec.zafirohome.datamodels.UnidadVolumenDto
import com.unitec.zafirohome.datamodels.UnidadVolumenRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ZafiroApiService {

    // --- AUTH ---
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // --- MARCAS ---
    @GET("/brands/get_all")
    suspend fun getMarcas(): ResponseWithData<MarcaDto>

    @POST("/brands/insert")
    suspend fun insertMarca(@Body request: MarcaRequest): EmptyResponse

    @POST("/brands/update")
    suspend fun updateMarca(@Body request: MarcaRequest): EmptyResponse

    @POST("/brands/delete")
    suspend fun deleteMarca(@Body request: DeleteMarcaRequest): EmptyResponse

    // --- TALLAS (Sizes) ---
    @GET("/sizes/get_all")
    suspend fun getTallas(): ResponseWithData<TallaDto>

    @POST("/sizes/insert")
    suspend fun insertTalla(@Body request: TallaRequest): EmptyResponse

    @POST("/sizes/update")
    suspend fun updateTalla(@Body request: TallaRequest): EmptyResponse

    @POST("/sizes/delete")
    suspend fun deleteTalla(@Body request: DeleteTallaRequest): EmptyResponse

    // --- UNIDADES DE VOLUMEN ---
    @GET("/units/get_all")
    suspend fun getUnidadesVolumen(): ResponseWithData<UnidadVolumenDto>

    @POST("/units/insert")
    suspend fun insertUnidadVolumen(@Body request: UnidadVolumenRequest): EmptyResponse

    @POST("/units/update")
    suspend fun updateUnidadVolumen(@Body request: UnidadVolumenRequest): EmptyResponse

    @POST("/units/delete")
    suspend fun deleteUnidadVolumen(@Body request: DeleteUnidadRequest): EmptyResponse

    // --- CAJAS ---
    @GET("/cajas/get_all")
    suspend fun getCajas(): ResponseWithData<CajaDto>

    @POST("/cajas/insert")
    suspend fun insertCaja(@Body data: CajaRequest): EmptyResponse

    @PUT("/cajas/update")
    suspend fun updateCaja(@Body data: CajaRequest): EmptyResponse

    @DELETE("/cajas/delete/{id}")
    suspend fun deleteCaja(@Path("id") id: Int): EmptyResponse

    // --- PRODUCTOS ---
    @GET("/productos/get_all")
    suspend fun getProductos(): ResponseWithData<ProductoDto>

    @POST("/productos/insert")
    suspend fun insertProducto(@Body data: ProductoRequest): EmptyResponse

    @POST("/productos/update")
    suspend fun updateProducto(@Body data: ProductoRequest): EmptyResponse

    @POST("/productos/delete")
    suspend fun deleteProducto(@Body data: DeleteProductoRequest): EmptyResponse

    @GET("/productos/buscar_por_nombre/{query}")
    suspend fun buscarProducto(@Path("query") query: String): ResponseWithData<ProductoDto>

    // --- CATEGORIAS ---

    // 1. Principales
    @GET("/categories/get_principal")
    suspend fun getPrincipal(): ResponseWithData<CategoriaPrincipalDto>

    @POST("/categories/insert_principal")
    suspend fun insertPrincipal(@Body data: CategoriaRequest): EmptyResponse

    @POST("/categories/update_principal")
    suspend fun updatePrincipal(@Body data: CategoriaRequest): EmptyResponse

    @POST("/categories/delete_principal")
    suspend fun deletePrincipal(@Body data: DeletePrincipalRequest): EmptyResponse

    // 2. Secundarias

    // 2. Secundarias
    @GET("/categories/get_secundaria/{id}")
    suspend fun getSecundaria(): ResponseWithData<CategoriaSecundariaDto>

    @GET("/categories/get_secundaria/{id}")
    suspend fun getSecundariabyId(@Path("id") idPrincipal: Int): ResponseWithData<CategoriaSecundariaDto>

    @POST("/categories/insert_secundaria")
    suspend fun insertSecundaria(@Body data: CategoriaRequest): EmptyResponse

    @POST("/categories/update_secundaria")
    suspend fun updateSecundaria(@Body data: CategoriaRequest): EmptyResponse

    @POST("/categories/delete_secundaria")
    suspend fun deleteSecundaria(@Body data: DeleteSecundariaRequest): EmptyResponse

    // 3. Subcategorías

    @GET("/categories/get_subcategoria/{id}")
    suspend fun getSubcategoria(): ResponseWithData<SubcategoriaDto>

    @GET("/categories/get_subcategoria/{id}")
    suspend fun getSubcategoriabyId(@Path("id") idSecundaria: Int): ResponseWithData<SubcategoriaDto>

    @POST("/categories/insert_subcategoria")
    suspend fun insertSubcategoria(@Body data: CategoriaRequest): EmptyResponse

    @POST("/categories/update_subcategoria")
    suspend fun updateSubcategoria(@Body data: CategoriaRequest): EmptyResponse

    @POST("/categories/delete_subcategoria")
    suspend fun deleteSubcategoria(@Body data: DeleteSubRequest): EmptyResponse

    // --- IMÁGENES ---
    // Usamos Multipart para enviar archivos binarios
    // Devolvemos Response<ImageResponse> para que el ViewModel pueda checar .isSuccessful y .body()
    @Multipart
    @POST("/imagenes")
    suspend fun subirImagen(
        @Part imagen: MultipartBody.Part,
        @Part("producto_id") id: RequestBody
    ): Response<ImageResponse>

}