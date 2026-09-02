package com.example.cicloud.utils

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

/**
 * Cierra la aplicación de forma nativa.
 */
expect fun exitApp()

/**
 * Abre una URL en el navegador del sistema o tienda.
 */
expect fun openUrl(url: String)
