package com.unitec.zafirohome.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unitec.zafirohome.datamodels.*
import com.unitec.zafirohome.network.RetrofitClient
import kotlinx.coroutines.launch

class TallasViewModel : ViewModel() {
    var listaTallas by mutableStateOf<List<TallaDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var showAddEditDialog by mutableStateOf(false)
    var showDeleteDialog by mutableStateOf(false)
    var itemSeleccionado by mutableStateOf<TallaDto?>(null)

    init { cargarTallas() }

    fun cargarTallas() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val res = RetrofitClient.api.getTallas()
                listaTallas = res.data ?: emptyList()
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally { isLoading = false }
        }
    }

    fun guardarTalla(nombre: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val req = TallaRequest(id = itemSeleccionado?.id, nombre = nombre)
                val res = if (itemSeleccionado == null) RetrofitClient.api.insertTalla(req)
                else RetrofitClient.api.updateTalla(req)

                if (res.success) {
                    showAddEditDialog = false
                    itemSeleccionado = null
                    cargarTallas()
                } else errorMessage = res.message
            } catch (e: Exception) { errorMessage = e.message }
            finally { isLoading = false }
        }
    }

    fun prepararEliminar(item: TallaDto) {
        itemSeleccionado = item
        showDeleteDialog = true
    }

    fun confirmarEliminar() {
        val item = itemSeleccionado ?: return
        viewModelScope.launch {
            isLoading = true
            try {
                val res = RetrofitClient.api.deleteTalla(DeleteTallaRequest(item.id))
                if (res.success) {
                    showDeleteDialog = false
                    itemSeleccionado = null
                    cargarTallas()
                } else errorMessage = res.message
            } catch (e: Exception) { errorMessage = e.message }
            finally { isLoading = false }
        }
    }
}