package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BabyRosePeach,
    onPrimary = Color.White,
    secondary = BabySkyBlue,
    onSecondary = Color.White,
    tertiary = BabySunYellow,
    background = Color(0xFF1E1E24),
    surface = Color(0xFF2A2D3A),
    surfaceVariant = Color(0xFF373A4A),
    onBackground = Color(0xFFF7F7FA),
    onSurface = Color(0xFFF7F7FA),
    outline = Color(0xFF555869)
)

private val LightColorScheme = lightColorScheme(
    primary = BabyRosePeach,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFECEF),
    onPrimaryContainer = BabyRoseDark,
    secondary = BabySkyBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE5F4FC),
    onSecondaryContainer = Color(0xFF006D9E),
    tertiary = BabySunYellow,
    onTertiary = Color(0xFF453000),
    tertiaryContainer = Color(0xFFFFF7DD),
    background = BabyMilkBg,
    onBackground = BabyTextPrimary,
    surface = BabySurfaceWhite,
    onSurface = BabyTextPrimary,
    surfaceVariant = BabySurfaceTint,
    onSurfaceVariant = BabyTextSecondary,
    outline = BabyCardBorder,
    outlineVariant = Color(0xFFF5EBE6)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
