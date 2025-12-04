package com.unitec.zafirohome.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Modelo de datos para los módulos (equivalente a tus divs .category-card)
data class AdminModule(
    val title: String,
    val icon: ImageVector,
    val route: String // Usaremos esto luego para navegar a Cajas, Marcas, etc.
)

@Composable
fun AdminScreen(
    onNavigateToModule: (String) -> Unit,
    onGoToStore: () -> Unit
) {
    // Definimos la lista de módulos basada en tu HTML
    val modules = listOf(
        AdminModule("Cajas", Icons.Default.Inventory2, "cajas"), // fa-box
        AdminModule("Marcas", Icons.Default.Style, "marcas"), // fa-tags
        AdminModule("Categorías", Icons.Default.Category, "categorias"), // fa-layer-group
        AdminModule("Unidades Tamaño.", Icons.Default.Straighten, "unidades_tam"), // fa-ruler
        AdminModule("Unidades Volumen.", Icons.Default.Scale, "unidades_vol"), // fa-balance-scale
        AdminModule("Usuarios", Icons.Default.Group, "usuarios"), // fa-users
        AdminModule("Productos", Icons.Default.Search, "productos") // fa-search
    )

    Scaffold(
        topBar = { AdminTopBar(onGoToStore) },
        containerColor = MaterialTheme.colorScheme.background // #F6F7FB
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // --- Header del Panel (Título y Descripción) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PANEL DE ADMINISTRACIÓN",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Administra marcas, categorías, unidades, tallas, usuarios y productos — todo desde un solo lugar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 600.dp)
                )
                // Línea decorativa naranja (accent)
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(3.dp)
                        .background(MaterialTheme.colorScheme.tertiary) // Naranja Acento
                )
            }

            // --- Grid de Módulos ---
            Text(
                text = "Módulos Administrativos",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            LazyVerticalGrid(
                // Adaptativo: 2 columnas en celular, más en tablet
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(modules) { module ->
                    AdminModuleCard(module, onClick = { onNavigateToModule(module.route) })
                }

                // Espacio extra al final para el footer visual
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

// --- Componentes Auxiliares ---

@Composable
fun AdminTopBar(onGoToStore: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.primary, // Azul Fuerte
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo y Título
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icono simulando el logo de la imagen
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Zafiro Home",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Composable
fun AdminModuleCard(module: AdminModule, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // Toda la tarjeta es clickeable para mejor UX móvil
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Cabecera de la Tarjeta (Rojo/Secundario)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary) // Rojo Fuerte
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = module.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Línea naranja decorativa debajo del header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            )

            // Cuerpo de la Tarjeta
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono Grande
                Icon(
                    imageVector = module.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary, // Rojo
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón "Entrar" (Visual, ya que toda la card es clickeable)
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary // Azul
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("ENTRAR")
                }
            }
        }
    }
}