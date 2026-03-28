package com.example.cicloud

import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores unificada para el proyecto Cicloud.
 */
object ColorConstants {
    // Colores de Marca / Principales
    val Primary = Color(0xFF1976D2)
    val Secondary = Color(0xFF42A5F5)
    
    // Colores de Estado (Sincronizados con Backend)
    val Success = Color(0xFF4CAF50) // Para "Exito!"
    val Warning = Color(0xFFFF9800) // Para "Alerta!!"
    val Error = Color(0xFFF44336)   // Para "Error!"
    val Info = Color(0xFF2196F3)    // Para mensajes informativos
    
    // Colores de Fondo y Superficie
    val Background = Color(0xFFF5F5F5)
    val Surface = Color(0xFFFFFFFF)
    
    // Colores de Texto
    val OnPrimary = Color(0xFFFFFFFF)
    val OnBackground = Color(0xFF212121)
}
