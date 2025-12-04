package com.unitec.zafirohome.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unitec.zafirohome.datamodels.*
import com.unitec.zafirohome.network.RetrofitClient
import kotlinx.coroutines.launch


class MarcasViewModel : ViewModel() {

    // --- ESTADO UI ---
    var listaMarcas: List<MarcaDto> by mutableStateOf(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // --- ESTADO MODALES ---
    var showAddEditDialog by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)
    var itemSeleccionado by mutableStateOf<MarcaDto?>(null)

    init {
        cargarMarcas()
    }

    fun cargarMarcas() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitClient.api.getMarcas()
                // Fix: Use elvis operator here to ensure it's never null
                listaMarcas = response.data ?: emptyList()
            } catch (e: Exception) {
                errorMessage = "Error al cargar marcas: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun guardarMarca(nombre: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val api = RetrofitClient.api
                val request = MarcaRequest(
                    id = itemSeleccionado?.id, // Si es null = INSERT, si tiene ID = UPDATE
                    nombre = nombre
                )

                val response = if (itemSeleccionado == null) {
                    api.insertMarca(request)
                } else {
                    api.updateMarca(request)
                }

                if (response.success) {
                    showAddEditDialog = false
                    itemSeleccionado = null
                    cargarMarcas() // Recargar lista
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Error al guardar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun prepararEliminar(item: MarcaDto) {
        itemSeleccionado = item
        showDeleteDialog = true
    }

    fun confirmarEliminar() {
        val item = itemSeleccionado ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.deleteMarca(DeleteMarcaRequest(item.id))
                if (response.success) {
                    showDeleteDialog = false
                    itemSeleccionado = null
                    cargarMarcas()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Error al eliminar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}