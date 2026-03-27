package com.example.cicloud

object Endpoints {
    // Auth
    const val AUTH_WEB = "/api/Auth/web"
    
    // Institución
    const val INSTITUCION_AUTOCOMPLETE = "/api/Institucion/autocomplete"
    
    // Función de ayuda para obtener la URL completa
    fun getUrl(endpoint: String): String {
        return "${ApiConfig.baseUrl}$endpoint"
    }
}
