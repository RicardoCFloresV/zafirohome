package com.unitec.zafirohome.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unitec.zafirohome.network.RetrofitClient
// Make sure this import is present to resolve 'ImageResponse' properties
import com.unitec.zafirohome.datamodels.ImageResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class ImageUploadViewModel : ViewModel() {

    var uploadStatus by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    fun uploadSelectedImage(context: Context, uri: Uri, productoId: Int) {
        viewModelScope.launch {
            isLoading = true
            uploadStatus = "Procesando imagen..."

            try {
                val file = uriToFile(context, uri)
                if (file == null) {
                    uploadStatus = "Error al leer el archivo local"
                    isLoading = false
                    return@launch
                }

                // Crear partes del Multipart
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val idBody = productoId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                // Usamos el Singleton directamente
                val response = RetrofitClient.api.subirImagen(body, idBody)

                if (response.isSuccessful) {
                    // response.body() is ImageResponse?
                    val serverMsg = response.body()?.message ?: "Éxito"
                    uploadStatus = "Subida correcta: $serverMsg"
                } else {
                    uploadStatus = "Error del servidor: ${response.code()} - ${response.message()}"
                }

            } catch (e: Exception) {
                uploadStatus = "Error de conexión: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}