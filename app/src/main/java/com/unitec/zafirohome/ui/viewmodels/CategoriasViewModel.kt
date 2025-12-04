package com.unitec.zafirohome.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unitec.zafirohome.datamodels.*
import com.unitec.zafirohome.network.RetrofitClient
import kotlinx.coroutines.launch

class CategoriasViewModel : ViewModel() {

    // --- ESTADO DE NAVEGACIÓN ---
    var nivelActual by mutableStateOf(NivelCategoria.PRINCIPAL)

    // Guardamos el ID del padre para poder listar los hijos
    // Si estamos en SECUNDARIA, este es el ID de la Principal seleccionada
    // Si estamos en SUBCATEGORIA, este es el ID de la Secundaria seleccionada
    var idPadreActual by mutableStateOf<Int?>(null)

    // --- ESTADO DE DATOS (UI) ---
    var listaItems by mutableStateOf<List<CategoriaUiItem>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // --- ESTADO DE MODALES ---
    var showAddEditDialog by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)

    // Item seleccionado para editar o eliminar
    var itemSeleccionado by mutableStateOf<CategoriaUiItem?>(null)

    init {
        cargarDatos()
    }

    // --- NAVEGACIÓN ---

    // Función para "entrar" en una categoría (Principal -> Secundaria -> Sub)
    fun entrarEnCategoria(item: CategoriaUiItem) {
        when (nivelActual) {
            NivelCategoria.PRINCIPAL -> {
                idPadreActual = item.id
                nivelActual = NivelCategoria.SECUNDARIA
                cargarDatos()
            }
            NivelCategoria.SECUNDARIA -> {
                idPadreActual = item.id
                nivelActual = NivelCategoria.SUBCATEGORIA
                cargarDatos()
            }
            NivelCategoria.SUBCATEGORIA -> {
                // No hay más niveles
            }
        }
    }

    // Función para volver atrás (Botón Back)
    fun regresarNivel() {
        when (nivelActual) {
            NivelCategoria.SUBCATEGORIA -> {
                // Simplificación: Volvemos al inicio o nivel anterior.
                // Nota: Para volver exacto a Secundaria necesitaríamos el ID de la Principal anterior.
                // Aquí reseteamos a Principal para evitar complejidad de "Pila de Navegación".
                nivelActual = NivelCategoria.PRINCIPAL
                idPadreActual = null
                cargarDatos()
            }
            NivelCategoria.SECUNDARIA -> {
                nivelActual = NivelCategoria.PRINCIPAL
                idPadreActual = null
                cargarDatos()
            }
            NivelCategoria.PRINCIPAL -> { /* No hace nada o cierra app */ }
        }
    }

    // --- CARGA DE DATOS ---

    fun cargarDatos() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val api = RetrofitClient.api
                val items = mutableListOf<CategoriaUiItem>()

                when (nivelActual) {
                    NivelCategoria.PRINCIPAL -> {
                        // getPrincipal devuelve ResponseWithData<CategoriaPrincipalDto>
                        val res = api.getPrincipal()
                        res.data?.forEach { dto ->
                            items.add(CategoriaUiItem(dto.id, dto.nombre, dto.descripcion, NivelCategoria.PRINCIPAL))
                        }
                    }
                    NivelCategoria.SECUNDARIA -> {
                        val id = idPadreActual ?: 0
                        val res = api.getSecundariabyId(id)
                        res.data?.forEach { dto ->
                            items.add(CategoriaUiItem(dto.id, dto.nombre, null, NivelCategoria.SECUNDARIA))
                        }
                    }
                    NivelCategoria.SUBCATEGORIA -> {
                        val id = idPadreActual ?: 0
                        val res = api.getSubcategoriabyId(id)
                        res.data?.forEach { dto ->
                            items.add(CategoriaUiItem(dto.id, dto.nombre, null, NivelCategoria.SUBCATEGORIA))
                        }
                    }
                }
                listaItems = items

            } catch (e: Exception) {
                errorMessage = "Error cargando datos: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // --- CRUD ---

    fun guardarCategoria(nombre: String, descripcion: String?) {
        viewModelScope.launch {
            isLoading = true
            try {
                val api = RetrofitClient.api

                // Creamos el request. Nota: Tu modelo CategoriaRequest actual
                // no incluye id del padre, asegúrate que el backend lo maneje o actualiza el modelo.
                val req = CategoriaRequest(
                    id = itemSeleccionado?.id,
                    nombre = nombre,
                    descripcion = if (nivelActual == NivelCategoria.PRINCIPAL) descripcion else null
                )

                val res: EmptyResponse = when (nivelActual) {
                    NivelCategoria.PRINCIPAL -> {
                        if (itemSeleccionado == null) api.insertPrincipal(req) else api.updatePrincipal(req)
                    }
                    NivelCategoria.SECUNDARIA -> {
                        if (itemSeleccionado == null) api.insertSecundaria(req) else api.updateSecundaria(req)
                    }
                    NivelCategoria.SUBCATEGORIA -> {
                        if (itemSeleccionado == null) api.insertSubcategoria(req) else api.updateSubcategoria(req)
                    }
                }

                if (res.success) {
                    showAddEditDialog = false
                    itemSeleccionado = null
                    cargarDatos()
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

    fun prepararEliminar(item: CategoriaUiItem) {
        itemSeleccionado = item
        showDeleteDialog = true
    }

    fun confirmarEliminar() {
        val item = itemSeleccionado ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                val api = RetrofitClient.api
                val res: EmptyResponse = when (nivelActual) {
                    NivelCategoria.PRINCIPAL -> api.deletePrincipal(DeletePrincipalRequest(item.id))
                    NivelCategoria.SECUNDARIA -> api.deleteSecundaria(DeleteSecundariaRequest(item.id))
                    NivelCategoria.SUBCATEGORIA -> api.deleteSubcategoria(DeleteSubRequest(item.id))
                }

                if (res.success) {
                    showDeleteDialog = false
                    itemSeleccionado = null
                    cargarDatos()
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