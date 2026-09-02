package com.example.cicloud.repository

import com.example.cicloud.network.Endpoints
import com.example.cicloud.BuildValues
import com.example.cicloud.models.InstitucionDto
import com.example.cicloud.models.auth.ChangePasswordRequest
import com.example.cicloud.models.auth.LoginRequest
import com.example.cicloud.models.auth.LoginResponseDto
import com.example.cicloud.network.process
import com.example.cicloud.network.processBase
import com.example.cicloud.network.handleBaseResponse
import com.example.cicloud.utils.getPlatform
import com.example.cicloud.GlobalMessageManager
import com.example.cicloud.models.VersionResponse
import io.ktor.client.*
import io.ktor.client.request.*

class AuthRepository(private val client: HttpClient) {
    
    suspend fun getInstituciones(query: String? = null): List<InstitucionDto>? {
        return client.get(Endpoints.getUrl(Endpoints.INSTITUCION_AUTOCOMPLETE)) {
            if (!query.isNullOrBlank()) {
                parameter("term", query)
            }
        }.process<List<InstitucionDto>>()
    }

    suspend fun login(request: LoginRequest): LoginResponseDto? {
        return client.post(Endpoints.getUrl(Endpoints.AUTH_MOBILE)) {
            setBody(request)
        }.process<LoginResponseDto>()
    }

    suspend fun cambiarPassword(request: ChangePasswordRequest): Boolean {
        return client.put(Endpoints.getUrl(Endpoints.CHANGE_PASSWORD)) {
            setBody(request)
        }.processBase()
    }

    suspend fun checkVersion(): Boolean {
        try {
            val platformName = getPlatform().name.lowercase()

            // Desactivar la verificación y alerta de actualización forzada para Android
            if (platformName.contains("android")) {
                println("--- DEBUG: Alerta de actualización forzada desactivada para Android ---")
                return true
            }

            val endpoint = Endpoints.VERSION_IOS
            println("--- DEBUG: Verificando versión en $endpoint ---")
            val res = client.get(Endpoints.getUrl(endpoint)).process<VersionResponse>()
            
            if (res != null) {
                val sVersion = res.version
                val sCode = res.code
                val lVersion = BuildValues.VERSION_NAME
                val lCode = BuildValues.VERSION_CODE.toString()

                println("--- DEBUG: Server($sVersion, $sCode) vs Local($lVersion, $lCode) ---")

                val sCodeInt = sCode.toIntOrNull()
                val lCodeInt = lCode.toIntOrNull()
                
                val isUpdateRequired = if (sCodeInt != null && lCodeInt != null) {
                    sCodeInt > lCodeInt || isVersionGreater(sVersion, lVersion)
                } else {
                    isVersionGreater(sCode, lCode) || isVersionGreater(sVersion, lVersion)
                }

                if (isUpdateRequired) {
                    println("--- DEBUG: Actualización detectada como OBLIGATORIA ---")
                    val currentUrl = res.downloadUrl
                    
                    GlobalMessageManager.show(
                        title = "ACTUALIZACIÓN OBLIGATORIA",
                        message = "Se requiere la versión ${res.version} para continuar. Por favor, descargue el nuevo APK.",
                        severity = "warn",
                        dismissable = false
                    ) {
                        if (!currentUrl.isNullOrBlank()) {
                            com.example.cicloud.utils.openUrl(currentUrl)
                        }
                        com.example.cicloud.utils.exitApp()
                    }
                    return false
                }
            } else {
                println("--- DEBUG: No se recibió respuesta del servidor de versiones ---")
            }
        } catch (e: Exception) {
            println("--- DEBUG: Error en checkVersion: ${e.message} ---")
        }
        return true
    }

    /**
     * Compara dos versiones en formato string (ej. "1.0.1" vs "1.0").
     * Retorna true si v1 > v2.
     */
    private fun isVersionGreater(v1: String, v2: String): Boolean {
        val parts1 = v1.split(".").mapNotNull { it.toIntOrNull() }
        val parts2 = v2.split(".").mapNotNull { it.toIntOrNull() }
        
        val length = maxOf(parts1.size, parts2.size)
        for (i in 0 until length) {
            val p1 = parts1.getOrElse(i) { 0 }
            val p2 = parts2.getOrElse(i) { 0 }
            if (p1 > p2) return true
            if (p1 < p2) return false
        }
        return false
    }
}
