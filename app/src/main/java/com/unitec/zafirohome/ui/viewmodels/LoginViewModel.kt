package com.unitec.zafirohome.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unitec.zafirohome.datamodels.LoginRequest
import com.unitec.zafirohome.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel : ViewModel() {

    // --- ESTADO DE LA UI ---
    var loginInput by mutableStateOf("")
    var passwordInput by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // Estados para navegación post-login
    var loginSuccess by mutableStateOf(false)
    var isAdmin by mutableStateOf(false)

    fun performLogin() {
        // 1. Validaciones locales
        if (loginInput.isBlank()) {
            errorMessage = "Ingresa tu usuario o correo"
            return
        }
        if (passwordInput.isBlank()) {
            errorMessage = "Ingresa tu contraseña"
            return
        }

        isLoading = true
        errorMessage = null

        // 2. Llamada a la API
        viewModelScope.launch {
            try {
                // Usamos el cliente Singleton
                val response = RetrofitClient.api.login(
                    LoginRequest(login = loginInput, password = passwordInput)
                )

                if (response.success) {
                    // ÉXITO: Guardamos rol y avisamos a la vista
                    isAdmin = response.isAdmin == true
                    loginSuccess = true
                } else {
                    // ERROR DE CREDENCIALES
                    errorMessage = response.message ?: "Credenciales incorrectas"
                }

            } catch (e: IOException) {
                // ERROR DE CONEXIÓN (Sin internet o servidor apagado)
                errorMessage = "No se pudo conectar al servidor. Revisa tu internet."
                e.printStackTrace()
            } catch (e: Exception) {
                // OTROS ERRORES
                errorMessage = "Error inesperado: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}