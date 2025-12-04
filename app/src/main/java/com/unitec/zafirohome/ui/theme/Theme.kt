package com.unitec.zafirohome.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema Oscuro (Usa los tonos pasteles definidos en Color.kt para contraste)
private val DarkColorScheme = darkColorScheme(
    primary = AzulPrimarioClaro,
    onPrimary = AzulPrimario, // Texto oscuro sobre azul claro
    secondary = RojoClaro,
    onSecondary = RojoFuerte,
    tertiary = NaranjaClaro,

    background = FondoOscuro,
    onBackground = TextoPrincipalOscuro,

    surface = SuperficieOscura,
    onSurface = TextoPrincipalOscuro,

    surfaceVariant = BordeOscuro // Útil para bordes en modo oscuro
)

// Esquema Claro (Colores corporativos fuertes)
private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = TextoClaro, // Blanco sobre Azul
    secondary = RojoFuerte,
    onSecondary = TextoClaro, // Blanco sobre Rojo
    tertiary = NaranjaAcento,

    background = FondoClaro, // Gris suave #F6F7FB
    onBackground = TextoPrincipal,

    surface = SuperficieBlanca, // Blanco para las Cards
    onSurface = TextoPrincipal,

    surfaceVariant = Borde // Para bordes de inputs o divisores
)

@Composable
fun ZafiroHomeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),

    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && true -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Configuración de la Barra de Estado (Status Bar)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Pinta la barra superior del color primario (Azul) o background
            window.statusBarColor = colorScheme.primary.toArgb()

            // Controla si los iconos (hora, batería) son claros u oscuros
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}