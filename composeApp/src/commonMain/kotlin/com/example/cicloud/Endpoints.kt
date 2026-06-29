package com.example.cicloud

import com.example.cicloud.network.ApiConfig

object Endpoints {
    // Auth
    const val AUTH_WEB = "/api/Auth/web"
    // Listar Registros asistencia
    const val ASISTENCIA_LISTAR = "/api/asistencia/listar-marcaciones"

    // Institución
    const val INSTITUCION_AUTOCOMPLETE = "/api/Institucion/autocomplete"
    
    // Función de ayuda para obtener la URL completa asegurando una sola barra
    fun getUrl(endpoint: String): String {
        val base = ApiConfig.baseUrl.removeSuffix("/")
        val path = endpoint.removePrefix("/")
        return "$base/$path"
    }
}
