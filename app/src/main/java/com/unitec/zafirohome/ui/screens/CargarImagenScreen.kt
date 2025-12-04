package com.unitec.zafirohome.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.unitec.zafirohome.ui.viewmodels.ImageUploadViewModel

@Composable
fun CargarImagenScreen(
    viewModel: ImageUploadViewModel = viewModel(),
    productoId: Int? = null, // ID que viene de la pantalla anterior
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    // Si nos pasan un ID, lo usamos. Si no, texto vacío.
    var productoIdInput by remember { mutableStateOf(productoId?.toString() ?: "") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedUri = uri }
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                }
                Text(
                    "Subir Imagen",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text("Asignar imagen al producto", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Campo ID (Deshabilitado si viene pre-relleno para evitar errores)
            OutlinedTextField(
                value = productoIdInput,
                onValueChange = { if (productoId == null) productoIdInput = it }, // Solo editable si no viene ID
                label = { Text("ID del Producto") },
                enabled = productoId == null, // Bloqueado si viene del listado
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Galería
            Button(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Seleccionar de Galería")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Previsualización
            if (selectedUri != null) {
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedUri),
                        contentDescription = "Preview",
                        modifier = Modifier.size(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(Color.LightGray.copy(alpha=0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin imagen seleccionada", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Subir
            Button(
                enabled = selectedUri != null && productoIdInput.isNotEmpty() && !viewModel.isLoading,
                onClick = {
                    val pId = productoIdInput.toIntOrNull()
                    if (pId != null && selectedUri != null) {
                        viewModel.uploadSelectedImage(context, selectedUri!!, pId)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("SUBIR AL SERVIDOR", fontWeight = FontWeight.Bold)
                }
            }

            // Status
            viewModel.uploadStatus?.let { status ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if(status.contains("Exito") || status.contains("correcta"))
                            Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = status,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black
                    )
                }
            }
        }
    }
}