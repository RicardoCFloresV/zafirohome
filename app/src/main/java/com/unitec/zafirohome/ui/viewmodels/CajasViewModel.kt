package com.unitec.zafirohome.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unitec.zafirohome.datamodels.*
import com.unitec.zafirohome.network.RetrofitClient
import kotlinx.coroutines.launch

class CajasViewModel : ViewModel() {

    var listaCajas by mutableStateOf<List<CajaDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // Estados para el diálogo
    var showAddEditDialog by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)
    var itemSeleccionado by mutableStateOf<CajaDto?>(null)

    init {
        cargarCajas()
    }

    fun cargarCajas() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val res = RetrofitClient.api.getCajas()
                listaCajas = res.data ?: emptyList()
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun guardarCaja(letra: String, cara: String, nivel: String) {
        viewModelScope.launch {
            // Validaciones básicas
            val caraInt = cara.toIntOrNull()
            val nivelInt = nivel.toIntOrNull()

            if (letra.isBlank() || caraInt == null || nivelInt == null) {
                errorMessage = "Por favor ingresa datos válidos (Letra texto, Cara y Nivel números)"
                return@launch
            }

            isLoading = true
            try {
                val req = CajaRequest(
                    id = itemSeleccionado?.id,
                    letra = letra,
                    cara = caraInt,
                    nivel = nivelInt
                )

                // Si tiene ID usamos update (PUT), si no insert (POST)
                val res = if (itemSeleccionado == null) {
                    RetrofitClient.api.insertCaja(req)
                } else {
                    RetrofitClient.api.updateCaja(req)
                }

                if (res.success) {
                    showAddEditDialog = false
                    itemSeleccionado = null
                    cargarCajas()
                } else {
                    errorMessage = res.message
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun prepararEliminar(item: CajaDto) {
        itemSeleccionado = item
        showDeleteDialog = true
    }

    fun confirmarEliminar() {
        val item = itemSeleccionado ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                // Usamos el endpoint DELETE con Path
                val res = RetrofitClient.api.deleteCaja(item.id)
                if (res.success) {
                    showDeleteDialog = false
                    itemSeleccionado = null
                    cargarCajas()
                } else {
                    errorMessage = res.message
                }
            } catch (e: Exception) {
                errorMessage = "Error al eliminar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}