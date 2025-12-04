package com.unitec.zafirohome.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unitec.zafirohome.datamodels.*
import com.unitec.zafirohome.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ProductosViewModel : ViewModel() {

    // --- ESTADO DE DATOS PRINCIPALES ---
    var listaProductos by mutableStateOf<List<ProductoDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var searchQuery by mutableStateOf("")

    // --- ESTADO DE CATÁLOGOS (Para los Dropdowns) ---
    var marcas by mutableStateOf<List<MarcaDto>>(emptyList())
    var categorias by mutableStateOf<List<CategoriaPrincipalDto>>(emptyList())

    var secundarias by mutableStateOf<List<CategoriaSecundariaDto>>(emptyList())
    var subcategorias by mutableStateOf<List<SubcategoriaDto>>(emptyList())

    var tallas by mutableStateOf<List<TallaDto>>(emptyList())
    var unidades by mutableStateOf<List<UnidadVolumenDto>>(emptyList())
    var cajas by mutableStateOf<List<CajaDto>>(emptyList())

    // --- ESTADOS DE UI ---
    var showAddEditDialog by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)
    var itemSeleccionado by mutableStateOf<ProductoDto?>(null)

    init {
        cargarTodo()
    }

    // Carga productos Y catálogos en paralelo
    fun cargarTodo() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val api = RetrofitClient.api

                // 1. Cargar Productos
                val productosDeferred = async { api.getProductos() }

                // 2. Cargar Catálogos (Async para velocidad)
                val marcasDef = async { api.getMarcas() }
                val catDef = async { api.getPrincipal() }
                val secDef = async { api.getSecundaria() }
                val subDef = async { api.getSubcategoria() }
                val tallasDef = async { api.getTallas() }
                val uniDef = async { api.getUnidadesVolumen() }
                val cajasDef = async { api.getCajas() }

                // Esperar a productos primero para mostrar lista rápido
                val resProductos = productosDeferred.await()
                listaProductos = resProductos.data ?: emptyList()

                // Esperar al resto
                marcas = marcasDef.await().data ?: emptyList()
                categorias = catDef.await().data ?: emptyList()
                secundarias = secDef.await().data ?: emptyList()
                subcategorias = subDef.await().data ?: emptyList()
                tallas = tallasDef.await().data ?: emptyList()
                unidades = uniDef.await().data ?: emptyList()
                cajas = cajasDef.await().data ?: emptyList()

            } catch (e: Exception) {
                errorMessage = "Error cargando datos: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun buscar(query: String) {
        searchQuery = query
        if (query.isBlank()) {
            cargarTodo() // Recargar lista completa
            return
        }
        viewModelScope.launch {
            isLoading = true
            try {
                val res = RetrofitClient.api.buscarProducto(query)
                listaProductos = res.data ?: emptyList()
            } catch (e: Exception) {
                errorMessage = "Error en búsqueda: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun guardarProducto(req: ProductoRequest) {
        viewModelScope.launch {
            isLoading = true
            try {
                val api = RetrofitClient.api
                // Si el ID viene en el request, es update, si no insert
                val res = if (req.id == null) api.insertProducto(req) else api.updateProducto(req)

                if (res.success) {
                    showAddEditDialog = false
                    itemSeleccionado = null
                    // Recargar solo productos
                    val nuevos = api.getProductos()
                    listaProductos = nuevos.data ?: emptyList()
                } else {
                    errorMessage = res.message
                }
            } catch (e: Exception) {
                errorMessage = "Error guardando: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun prepararEliminar(item: ProductoDto) {
        itemSeleccionado = item
        showDeleteDialog = true
    }

    fun confirmarEliminar() {
        val item = itemSeleccionado ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                val res = RetrofitClient.api.deleteProducto(DeleteProductoRequest(item.id))
                if (res.success) {
                    showDeleteDialog = false
                    itemSeleccionado = null
                    val nuevos = RetrofitClient.api.getProductos()
                    listaProductos = nuevos.data ?: emptyList()
                } else {
                    errorMessage = res.message
                }
            } catch (e: Exception) {
                errorMessage = "Error eliminando: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}