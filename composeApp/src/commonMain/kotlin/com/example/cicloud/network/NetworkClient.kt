package com.example.cicloud.network

import com.example.cicloud.GlobalMessageManager
import com.example.cicloud.models.GeneralResponse
import com.example.cicloud.BuildValues
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SessionManager {
    var token: String? = null
    var institucionId: Int = 0
    var isLoggedIn by mutableStateOf(false)
}

/**
 * Cliente HTTP configurado para ser dinámico.
 * Los encabezados se evalúan en cada petición.
 */
val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }

    // Interceptor para encabezados dinámicos (Token e Institución)
    install(DefaultRequest) {
        header("Content-Type", "application/json")
    }

    // Usamos un plugin para inyectar headers en cada request
    install(HttpCallValidator) {
        validateResponse { response ->
            val statusCode = response.status.value
            when (statusCode) {
                401 -> {
                    GlobalMessageManager.show("SESIÓN INVÁLIDA", "INICIE SESIÓN O INGRESE NUEVAMENTE", "warn")
                    SessionManager.isLoggedIn = false
                }
                403 -> GlobalMessageManager.show("ACCIÓN NO PERMITIDA", "PERMISOS INSUFICIENTES", "error")
                500 -> GlobalMessageManager.show("ERROR", "Algo salió mal en el servidor", "error")
            }
        }
        
        handleResponseExceptionWithRequest { cause, _ ->
            GlobalMessageManager.show("SIN CONEXIÓN", "No podemos conectarnos al servicio", "error")
        }
    }
}.apply {
    // Interceptor manual para asegurar que los headers sean siempre los últimos valores de SessionManager
    plugin(HttpSend).intercept { request ->
        request.header("Institucion", SessionManager.institucionId.toString())
        SessionManager.token?.let {
            request.header("Authorization", "Bearer $it")
        }
        execute(request)
    }
}

suspend inline fun <reified T> HttpResponse.process(): T? {
    val responseBody = try {
        this.body<GeneralResponse<T>>()
    } catch (e: Exception) {
        null
    }

    if (responseBody != null) {
        val success = responseBody.success
        val showAlert = responseBody.showAlert
        val title = responseBody.titleMessage
        val text = responseBody.textMessage

        if (showAlert) {
            val severity = when (title) {
                "Exito!" -> "success"
                "Alerta!!" -> "warn"
                "Error!" -> "error"
                else -> if (success) "success" else "error"
            }
            GlobalMessageManager.show(title, text, severity)
        }

        if (!success) {
            throw Exception(text)
        }

        return responseBody.content
    }
    
    return null
}

object ApiConfig {
    val baseUrl = BuildValues.BACKEND_URL
}
