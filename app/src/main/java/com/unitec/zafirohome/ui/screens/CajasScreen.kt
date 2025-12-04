package com.unitec.zafirohome.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unitec.zafirohome.datamodels.CajaDto
import com.unitec.zafirohome.ui.viewmodels.CajasViewModel

@Composable
fun CajasScreen(
    viewModel: CajasViewModel = viewModel(),
    onBack: () -> Unit
) {
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
                    "Gestión de Cajas",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.itemSeleccionado = null
                    viewModel.showAddEditDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Caja", tint = Color.White)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            Column(modifier = Modifier.padding(16.dp)) {
                if (viewModel.errorMessage != null) {
                    Text(
                        text = viewModel.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.listaCajas) { caja ->
                        CajaCard(
                            item = caja,
                            onEdit = {
                                viewModel.itemSeleccionado = caja
                                viewModel.showAddEditDialog = true
                            },
                            onDelete = { viewModel.prepararEliminar(caja) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    // --- DIÁLOGO (3 CAMPOS) ---
    if (viewModel.showAddEditDialog) {
        // Variables temporales
        var letra by remember { mutableStateOf(viewModel.itemSeleccionado?.letra ?: "") }
        var cara by remember { mutableStateOf(viewModel.itemSeleccionado?.cara?.toString() ?: "") }
        var nivel by remember { mutableStateOf(viewModel.itemSeleccionado?.nivel?.toString() ?: "") }

        AlertDialog(
            onDismissRequest = { viewModel.showAddEditDialog = false },
            title = { Text(if (viewModel.itemSeleccionado == null) "Nueva Caja" else "Editar Caja") },
            text = {
                Column {
                    OutlinedTextField(
                        value = letra,
                        onValueChange = { letra = it },
                        label = { Text("Letra (Ej: A, B)") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = cara,
                            onValueChange = { if (it.all { char -> char.isDigit() }) cara = it },
                            label = { Text("Cara") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = nivel,
                            onValueChange = { if (it.all { char -> char.isDigit() }) nivel = it },
                            label = { Text("Nivel") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.guardarCaja(letra, cara, nivel) },
                    enabled = letra.isNotBlank() && cara.isNotBlank() && nivel.isNotBlank()
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showAddEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // --- DIÁLOGO ELIMINAR ---
    if (viewModel.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteDialog = false },
            title = { Text("Eliminar Caja") },
            text = {
                Text("¿Estás seguro de eliminar la caja '${viewModel.itemSeleccionado?.letra}' (Cara: ${viewModel.itemSeleccionado?.cara}, Nivel: ${viewModel.itemSeleccionado?.nivel})?")
            },
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

@Composable
fun CajaCard(
    item: CajaDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Caja ${item.letra}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = {},
                        label = { Text("Cara: ${item.cara}") },
                        colors = AssistChipDefaults.assistChipColors(labelColor = Color.DarkGray)
                    )
                    AssistChip(
                        onClick = {},
                        label = { Text("Nivel: ${item.nivel}") },
                        colors = AssistChipDefaults.assistChipColors(labelColor = Color.DarkGray)
                    )
                }
                Text(
                    text = "ID: ${item.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

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