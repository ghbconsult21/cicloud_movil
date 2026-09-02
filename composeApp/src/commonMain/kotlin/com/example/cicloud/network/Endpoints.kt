package com.example.cicloud.network

import com.example.cicloud.network.ApiConfig

object Endpoints {
    // Auth
    const val AUTH_MOBILE = "/api/Auth/mobile"
    const val CHANGE_PASSWORD = "/api/Auth/change-password"
    // Listar Registros asistencia
    const val ASISTENCIA_LISTAR = "/api/asistencia/listar-marcaciones"
    const val ASISTENCIA_REGISTRAR = "/api/asistencia/registrar-ingreso"
    const val ASISTENCIA_HORARIO = "/api/Asistencia/obtener-mi-horario"

    // Institución
    const val INSTITUCION_AUTOCOMPLETE = "/api/Institucion/autocomplete"

    // Versión
    const val VERSION_ANDROID = "/api/version/android"
    const val VERSION_IOS = "/api/version/ios"
    
    // Función de ayuda para obtener la URL completa asegurando una sola barra
    fun getUrl(endpoint: String): String {
        val base = ApiConfig.baseUrl.removeSuffix("/")
        val path = endpoint.removePrefix("/")
        return "$base/$path"
    }
}
