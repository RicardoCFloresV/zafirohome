package com.unitec.zafirohome

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

// --- IMPORTS DE PANTALLAS ---
import com.unitec.zafirohome.ui.screens.LoginScreen
import com.unitec.zafirohome.ui.screens.AdminScreen
import com.unitec.zafirohome.ui.screens.CategoriasScreen
import com.unitec.zafirohome.ui.screens.MarcasScreen
import com.unitec.zafirohome.ui.screens.CajasScreen
import com.unitec.zafirohome.ui.screens.CargarImagenScreen
import com.unitec.zafirohome.ui.screens.TallasScreen          // Corresponde a Unidades de Tamaño
import com.unitec.zafirohome.ui.screens.UnidadesVolumenScreen // Corresponde a Unidades de Volumen
import com.unitec.zafirohome.ui.screens.ProductosScreen

import com.unitec.zafirohome.ui.theme.ZafiroHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZafiroHomeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavegacionPrincipal(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NavegacionPrincipal(modifier: Modifier = Modifier) {
    // ESTADO DE NAVEGACIÓN
    // Controla qué pantalla se muestra actualmente
    var pantallaActual by remember { mutableStateOf("LOGIN") }
    var esAdmin by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Gestor de navegación simple (State Hoisting)
    when (pantallaActual) {

        // ------------------------------------------------
        // 1. PANTALLA DE INICIO DE SESIÓN
        // ------------------------------------------------
        "LOGIN" -> {
            LoginScreen(
                onLoginSuccess = { adminRole ->
                    esAdmin = adminRole
                    if (esAdmin) {
                        pantallaActual = "ADMIN_DASHBOARD"
                    } else {
                        pantallaActual = "USER_HOME"
                        Toast.makeText(context, "Vista de Usuario no implementada", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        // ------------------------------------------------
        // 2. PANEL DE ADMINISTRADOR (GRID DE MÓDULOS)
        // ------------------------------------------------
        "ADMIN_DASHBOARD" -> {
            AdminScreen(
                onNavigateToModule = { route ->
                    // Mapeamos las rutas definidas en AdminScreen a nuestros estados de pantalla
                    when (route) {
                        "categorias"   -> pantallaActual = "CATEGORIAS_MANAGER"
                        "marcas"       -> pantallaActual = "MARCAS_MANAGER"
                        "cajas"        -> pantallaActual = "CAJAS_MANAGER"
                        "unidades_tam" -> pantallaActual = "TALLAS_MANAGER"
                        "unidades_vol" -> pantallaActual = "UNIDADES_VOLUMEN_MANAGER"
                        "productos"    -> pantallaActual = "PRODUCTOS_MANAGER"

                        // Módulos pendientes
                        "usuarios"     -> Toast.makeText(context, "Módulo Usuarios en construcción", Toast.LENGTH_SHORT).show()

                        else -> Toast.makeText(context, "Ruta desconocida: $route", Toast.LENGTH_SHORT).show()
                    }
                },
                onGoToStore = {
                    pantallaActual = "LOGIN" // Cerrar sesión / Ir a tienda
                }
            )
        }

        // ------------------------------------------------
        // 3. PANTALLAS DE GESTIÓN (CRUDs)
        // ------------------------------------------------

        "CATEGORIAS_MANAGER" -> {
            CategoriasScreen(
                onBack = { pantallaActual = "ADMIN_DASHBOARD" }
            )
        }

        "MARCAS_MANAGER" -> {
            MarcasScreen(
                onBack = { pantallaActual = "ADMIN_DASHBOARD" }
            )
        }

        "CAJAS_MANAGER" -> {
            CajasScreen(
                onBack = { pantallaActual = "ADMIN_DASHBOARD" }
            )
        }

        "TALLAS_MANAGER" -> {
            TallasScreen(
                onBack = { pantallaActual = "ADMIN_DASHBOARD" }
            )
        }

        "UNIDADES_VOLUMEN_MANAGER" -> {
            UnidadesVolumenScreen(
                onBack = { pantallaActual = "ADMIN_DASHBOARD" }
            )
        }

        // ------------------------------------------------
        // 4. PANTALLA DE USUARIO (Placeholder)
        // ------------------------------------------------
        "USER_HOME" -> {
            // Aquí iría la tienda para el usuario normal
            // Por ahora, redirigimos al login para no quedar atrapados
            LoginScreen(onLoginSuccess = { })
        }

        // --- PRODUCTOS Y NAVEGACIÓN A IMAGEN ---
        "PRODUCTOS_MANAGER" -> {
            ProductosScreen(
                onBack = { pantallaActual = "ADMIN_DASHBOARD" },
                onNavigateToImage = { prodId ->
                    var productoParaImagen = prodId // Guardamos ID
                    pantallaActual = "IMAGE_UPLOAD" // Cambiamos pantalla
                }
            )
        }

        "IMAGE_UPLOAD" -> {
            val productoParaImagen = null
            CargarImagenScreen(
                productoId = productoParaImagen,
                onBack = { pantallaActual = "PRODUCTOS_MANAGER" }
            )
        }

    }
}