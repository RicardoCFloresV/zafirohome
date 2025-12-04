package com.unitec.zafirohome.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unitec.zafirohome.datamodels.*
import com.unitec.zafirohome.ui.viewmodels.ProductosViewModel

@Composable
fun ProductosScreen(
    viewModel: ProductosViewModel = viewModel(),
    onBack: () -> Unit,
    onNavigateToImage: (Int) -> Unit // Nuevo parámetro: ID del producto
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                    Text(
                        "Gestión de Productos",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Barra de Búsqueda
                OutlinedTextField(
                    value = viewModel.searchQuery,
                    onValueChange = { viewModel.buscar(it) },
                    placeholder = { Text("Buscar producto...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(Color.White, MaterialTheme.shapes.medium),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = Color.White)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(MaterialTheme.colorScheme.background)) {

            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            Column(modifier = Modifier.padding(16.dp)) {
                if (viewModel.errorMessage != null) {
                    Text(viewModel.errorMessage!!, color = MaterialTheme.colorScheme.error)
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.listaProductos) { prod ->
                        ProductoCard(
                            item = prod,
                            onEdit = {
                                viewModel.itemSeleccionado = prod
                                viewModel.showAddEditDialog = true
                            },
                            onDelete = { viewModel.prepararEliminar(prod) },
                            onImageClick = { onNavigateToImage(prod.id) } // Callback de imagen
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    // --- MODALES (Formulario y Delete) ---
    if (viewModel.showAddEditDialog) {
        DialogoProducto(
            producto = viewModel.itemSeleccionado,
            viewModel = viewModel,
            onDismiss = { viewModel.showAddEditDialog = false },
            onConfirm = { req -> viewModel.guardarProducto(req) }
        )
    }

    if (viewModel.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteDialog = false },
            title = { Text("Eliminar Producto") },
            text = { Text("¿Eliminar '${viewModel.itemSeleccionado?.nombre}'?") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmarEliminar() }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun ProductoCard(
    item: ProductoDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onImageClick: () -> Unit // Nuevo callback
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- FOTO ---
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (item.imagenUrlCompleta != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imagenUrlCompleta)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        error = rememberVectorPainter(Icons.Default.BrokenImage),
                        placeholder = rememberVectorPainter(Icons.Default.Image)
                    )
                } else {
                    Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // --- INFO ---
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text("$${item.precio}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }

                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    item.marcaNombre?.let {
                        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.small, modifier = Modifier.padding(end = 6.dp)) {
                            Text(it, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Surface(color = if(item.stock<5) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small) {
                        Text("Stock: ${item.stock}", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
        }

        Divider(color = Color.LightGray.copy(alpha = 0.2f))

        // --- BOTONES ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            // Botón Subir Imagen
            TextButton(onClick = onImageClick) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Imagen")
            }
            TextButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Editar")
            }
            TextButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Eliminar", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

// Reutiliza DialogoProducto y MyDropdown del código anterior (no han cambiado, solo la Card y la firma de la función principal)
// Asegúrate de incluir la función DialogoProducto aquí abajo como en el ejemplo previo.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoProducto(
    producto: ProductoDto?,
    viewModel: ProductosViewModel,
    onDismiss: () -> Unit,
    onConfirm: (ProductoRequest) -> Unit
) {
    // ... (Mismo código de DialogoProducto que te di antes)
    // Para no hacer la respuesta infinita, asumo que mantienes esa función igual.
    // Si la necesitas de nuevo, dímelo.

    // Estados del formulario
    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }
    var precio by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var stock by remember { mutableStateOf(producto?.stock?.toString() ?: "") }

    // Estados de selección (Dropdowns)
    var selectedMarca by remember { mutableStateOf(viewModel.marcas.find { it.id == producto?.marcaId }) }
    var selectedCat by remember { mutableStateOf(viewModel.categorias.find { it.id == producto?.categoriaId }) }
    var selectedTalla by remember { mutableStateOf(viewModel.tallas.find { it.id == producto?.tallaId }) }
    var selectedUnidad by remember { mutableStateOf(viewModel.unidades.find { it.id == producto?.unidadId }) }
    var selectedCaja by remember { mutableStateOf(viewModel.cajas.find { it.id == producto?.cajaId }) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxHeight(0.9f),
        title = { Text(if (producto == null) "Nuevo Producto" else "Editar Producto") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = precio, onValueChange = { if(it.all { c -> c.isDigit() || c == '.' }) precio = it },
                        label = { Text("Precio") }, modifier = Modifier.weight(1f),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal)
                    )
                    OutlinedTextField(
                        value = stock, onValueChange = { if(it.all { c -> c.isDigit() }) stock = it },
                        label = { Text("Stock") }, modifier = Modifier.weight(1f),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Clasificación", style = MaterialTheme.typography.titleSmall)

                MyDropdown("Marca", viewModel.marcas, selectedMarca, { selectedMarca = it }, { it.nombre })
                MyDropdown("Categoría", viewModel.categorias, selectedCat, { selectedCat = it }, { it.nombre })
                MyDropdown("Talla", viewModel.tallas, selectedTalla, { selectedTalla = it }, { it.nombre })
                MyDropdown("Unidad", viewModel.unidades, selectedUnidad, { selectedUnidad = it }, { "${it.nombre} (${it.abreviacion})" })
                MyDropdown("Caja", viewModel.cajas, selectedCaja, { selectedCaja = it }, { "Caja ${it.letra} (N${it.nivel})" })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val req = ProductoRequest(
                        id = producto?.id,
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        stock = stock.toIntOrNull() ?: 0,
                        marcaId = selectedMarca?.id,
                        categoriaId = selectedCat?.id,
                        tallaId = selectedTalla?.id,
                        unidadId = selectedUnidad?.id,
                        cajaId = selectedCaja?.id,
                        categoriaSecId = null, subcategoriaId = null
                    )
                    onConfirm(req)
                },
                enabled = nombre.isNotBlank() && precio.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MyDropdown(
    label: String, options: List<T>, selectedOption: T?, onOptionSelected: (T) -> Unit, itemLabel: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()
    ) {
        OutlinedTextField(
            readOnly = true, value = if (selectedOption != null) itemLabel(selectedOption) else "Seleccione...",
            onValueChange = { }, label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(itemLabel(option)) }, onClick = { onOptionSelected(option); expanded = false })
            }
        }
    }
}