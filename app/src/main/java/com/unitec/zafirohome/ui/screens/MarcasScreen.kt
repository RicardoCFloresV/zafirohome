package com.unitec.zafirohome.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unitec.zafirohome.datamodels.MarcaDto
import com.unitec.zafirohome.ui.viewmodels.MarcasViewModel

@Composable
fun MarcasScreen(
    viewModel: MarcasViewModel = viewModel(),
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            // Reutilizamos el estilo de TopBar azul
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
                    "Gestión de Marcas",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.itemSeleccionado = null // Nuevo registro
                    viewModel.showAddEditDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Marca", tint = Color.White)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Gris suave
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            Column(modifier = Modifier.padding(16.dp)) {

                // Mensaje de error
                if (viewModel.errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = viewModel.errorMessage!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // Lista de Marcas
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.listaMarcas) { marca ->
                        MarcaItemCard(
                            item = marca,
                            onEdit = {
                                viewModel.itemSeleccionado = marca
                                viewModel.showAddEditDialog = true
                            },
                            onDelete = { viewModel.prepararEliminar(marca) }
                        )
                    }
                    // Espacio final para el FAB
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    // --- DIÁLOGOS ---

    // 1. Agregar / Editar
    if (viewModel.showAddEditDialog) {
        DialogoMarca(
            marca = viewModel.itemSeleccionado,
            onDismiss = { viewModel.showAddEditDialog = false },
            onConfirm = { nombre -> viewModel.guardarMarca(nombre) }
        )
    }

    // 2. Eliminar
    if (viewModel.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de eliminar la marca '${viewModel.itemSeleccionado?.nombre}'?") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmarEliminar() }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// --- SUBCOMPONENTES ---

@Composable
fun MarcaItemCard(
    item: MarcaDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "ID: ${item.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Botones de acción
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun DialogoMarca(
    marca: MarcaDto?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var nombre by remember { mutableStateOf(marca?.nombre ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (marca == null) "Nueva Marca" else "Editar Marca") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de la marca") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nombre) },
                enabled = nombre.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}