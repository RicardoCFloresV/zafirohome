package com.unitec.zafirohome.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unitec.zafirohome.datamodels.*
import com.unitec.zafirohome.network.RetrofitClient
import kotlinx.coroutines.launch

class UnidadesVolumenViewModel : ViewModel() {
    var listaUnidades by mutableStateOf<List<UnidadVolumenDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var showAddEditDialog by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)
    var itemSeleccionado by mutableStateOf<UnidadVolumenDto?>(null)

    init { cargarUnidades() }

    fun cargarUnidades() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val res = RetrofitClient.api.getUnidadesVolumen()
                listaUnidades = res.data ?: emptyList()
            } catch (e: Exception) { errorMessage = "Error: ${e.message}" }
            finally { isLoading = false }
        }
    }

    // Nota: Recibe nombre Y abreviacion
    fun guardarUnidad(nombre: String, abreviacion: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val req = UnidadVolumenRequest(
                    id = itemSeleccionado?.id,
                    nombre = nombre,
                    abreviacion = abreviacion
                )
                val res = if (itemSeleccionado == null) RetrofitClient.api.insertUnidadVolumen(req)
                else RetrofitClient.api.updateUnidadVolumen(req)

                if (res.success) {
                    showAddEditDialog = false
                    itemSeleccionado = null
                    cargarUnidades()
                } else errorMessage = res.message
            } catch (e: Exception) { errorMessage = e.message }
            finally { isLoading = false }
        }
    }

    fun prepararEliminar(item: UnidadVolumenDto) {
        itemSeleccionado = item
        showDeleteDialog = true
    }

    fun confirmarEliminar() {
        val item = itemSeleccionado ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                val res = RetrofitClient.api.deleteUnidadVolumen(DeleteUnidadRequest(item.id))
                if (res.success) {
                    showDeleteDialog = false
                    itemSeleccionado = null
                    cargarUnidades()
                } else errorMessage = res.message
            } catch (e: Exception) { errorMessage = e.message }
            finally { isLoading = false }
        }
    }
}