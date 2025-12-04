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
import com.unitec.zafirohome.datamodels.TallaDto
import com.unitec.zafirohome.ui.viewmodels.TallasViewModel

@Composable
fun TallasScreen(viewModel: TallasViewModel = viewModel(), onBack: () -> Unit) {
    Scaffold(
        topBar = {
            Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White) }
                Text("Gestión de Tallas", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.itemSeleccionado = null; viewModel.showAddEditDialog = true }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            Column(modifier = Modifier.padding(16.dp)) {
                if (viewModel.errorMessage != null) Text(viewModel.errorMessage!!, color = MaterialTheme.colorScheme.error)

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.listaTallas) { item ->
                        TallaCard(item, onEdit = { viewModel.itemSeleccionado = item; viewModel.showAddEditDialog = true }, onDelete = { viewModel.prepararEliminar(item) })
                    }
                }
            }
        }
    }

    if (viewModel.showAddEditDialog) {
        var nombre by remember { mutableStateOf(viewModel.itemSeleccionado?.nombre ?: "") }
        AlertDialog(
            onDismissRequest = { viewModel.showAddEditDialog = false },
            title = { Text(if (viewModel.itemSeleccionado == null) "Nueva Talla" else "Editar Talla") },
            text = { OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, singleLine = true) },
            confirmButton = { Button(onClick = { viewModel.guardarTalla(nombre) }, enabled = nombre.isNotBlank()) { Text("Guardar") } },
            dismissButton = { TextButton(onClick = { viewModel.showAddEditDialog = false }) { Text("Cancelar") } }
        )
    }

    if (viewModel.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteDialog = false },
            title = { Text("Eliminar") },
            text = { Text("¿Eliminar '${viewModel.itemSeleccionado?.nombre}'?") },
            confirmButton = { TextButton(onClick = { viewModel.confirmarEliminar() }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) } },
            dismissButton = { TextButton(onClick = { viewModel.showDeleteDialog = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
fun TallaCard(item: TallaDto, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("ID: ${item.id}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Row {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
            }
        }
    }
}