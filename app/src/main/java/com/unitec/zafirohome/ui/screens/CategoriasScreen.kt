package com.unitec.zafirohome.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.unitec.zafirohome.datamodels.CategoriaUiItem
import com.unitec.zafirohome.datamodels.NivelCategoria
import com.unitec.zafirohome.ui.viewmodels.CategoriasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    viewModel: CategoriasViewModel = viewModel(),
    onBack: () -> Unit
) {
    // Manejo del botón físico "Atrás" de Android
    BackHandler(enabled = true) {
        if (viewModel.nivelActual == NivelCategoria.PRINCIPAL) {
            onBack()
        } else {
            viewModel.regresarNivel()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (viewModel.nivelActual) {
                            NivelCategoria.PRINCIPAL -> "Categorías Principales"
                            NivelCategoria.SECUNDARIA -> "Categorías Secundarias"
                            NivelCategoria.SUBCATEGORIA -> "Subcategorías"
                        },
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (viewModel.nivelActual == NivelCategoria.PRINCIPAL) {
                            onBack()
                        } else {
                            viewModel.regresarNivel()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.itemSeleccionado = null // Limpiar selección para "Nuevo"
                    viewModel.showAddEditDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column {
                    // Mensaje de error si existe
                    viewModel.errorMessage?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    // Lista de Categorías
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.listaItems) { item ->
                            CategoriaItemCard(
                                item = item,
                                onClick = { viewModel.entrarEnCategoria(item) },
                                onEdit = {
                                    viewModel.itemSeleccionado = item
                                    viewModel.showAddEditDialog = true
                                },
                                onDelete = { viewModel.prepararEliminar(item) }
                            )
                        }
                    }
                }
            }
        }
    }

    // --- DIÁLOGO AGREGAR / EDITAR ---
    if (viewModel.showAddEditDialog) {
        DialogoCategoria(
            item = viewModel.itemSeleccionado,
            esPrincipal = viewModel.nivelActual == NivelCategoria.PRINCIPAL,
            onDismiss = { viewModel.showAddEditDialog = false },
            onConfirm = { nombre, desc ->
                viewModel.guardarCategoria(nombre, desc)
            }
        )
    }

    // --- DIÁLOGO CONFIRMAR BORRAR ---
    if (viewModel.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteDialog = false },
            title = { Text("Eliminar Categoría") },
            text = { Text("¿Estás seguro de eliminar '${viewModel.itemSeleccionado?.nombre}'? Se eliminarán también sus hijos.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmarEliminar() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
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
fun CategoriaItemCard(
    item: CategoriaUiItem,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Permite entrar al siguiente nivel
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (item.tipo == NivelCategoria.PRINCIPAL && !item.descripcion.isNullOrEmpty()) {
                    Text(
                        text = item.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                // Indicador visual de que se puede navegar
                if (item.tipo != NivelCategoria.SUBCATEGORIA) {
                    Text(
                        text = "Ver subniveles >",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
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

@Composable
fun DialogoCategoria(
    item: CategoriaUiItem?,
    esPrincipal: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String?) -> Unit
) {
    var nombre by remember { mutableStateOf(item?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(item?.descripcion ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Nueva Categoría" else "Editar Categoría") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (esPrincipal) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nombre, descripcion) },
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