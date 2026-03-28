package com.example.cicloud

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ColorConstants.Primary,
    secondary = ColorConstants.Secondary,
    tertiary = ColorConstants.Info,
    error = ColorConstants.Error,
    background = ColorConstants.OnBackground, // Ajuste para modo oscuro
    surface = ColorConstants.OnBackground,
    onPrimary = ColorConstants.OnPrimary,
    onBackground = ColorConstants.Background,
    onSurface = ColorConstants.Background,
)

private val LightColorScheme = lightColorScheme(
    primary = ColorConstants.Primary,
    secondary = ColorConstants.Secondary,
    tertiary = ColorConstants.Info,
    error = ColorConstants.Error,
    background = ColorConstants.Background,
    surface = ColorConstants.Surface,
    onPrimary = ColorConstants.OnPrimary,
    onSecondary = ColorConstants.OnPrimary,
    onTertiary = ColorConstants.OnPrimary,
    onBackground = ColorConstants.OnBackground,
    onSurface = ColorConstants.OnBackground,
)

@Composable
fun CicloudTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(), // Puedes personalizar tipografía aquí luego
        content = content
    )
}
