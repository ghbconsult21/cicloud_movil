package com.example.cicloud.network

import com.example.cicloud.GlobalMessageManager
import com.example.cicloud.models.GeneralResponse
import com.example.cicloud.models.BaseResponse
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
import kotlin.time.TimeSource
import kotlin.time.Duration.Companion.hours

expect fun HttpClientConfig<*>.configurePlatformEngine()

/**
 * Cliente HTTP configurado para ser dinámico.
 */
val httpClient = HttpClient {
    configurePlatformEngine()

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }

    install(HttpTimeout) {
        requestTimeoutMillis = BuildValues.TIMEOUT_SECONDS * 1000
        connectTimeoutMillis = BuildValues.TIMEOUT_SECONDS * 1000
        socketTimeoutMillis = BuildValues.TIMEOUT_SECONDS * 1000
    }

    install(DefaultRequest) {
        header("Content-Type", "application/json")
    }

    install(HttpCallValidator) {
        validateResponse { response ->
            val statusCode = response.status.value
            when (statusCode) {
                401 -> {
                    GlobalMessageManager.show("SESIÓN INVÁLIDA", "INICIE SESIÓN O INGRESE NUEVAMENTE", "warn")
                    SessionManager.resetSession()
                }
                403 -> GlobalMessageManager.show("ACCIÓN NO PERMITIDA", "PERMISOS INSUFICIENTES", "error")
                500 -> GlobalMessageManager.show("ERROR", "Algo salió mal en el servidor", "error")
            }
        }
        
        handleResponseExceptionWithRequest { cause, _ ->
            val technicalError = cause.message ?: "Error desconocido"
            GlobalMessageManager.show("SIN CONEXIÓN", technicalError, "error")
        }
    }
}.apply {
    plugin(HttpSend).intercept { request ->
        SessionManager.updateActivity()
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
        handleBaseResponse(responseBody.success, responseBody.showAlert, responseBody.titleMessage, responseBody.textMessage)
        return responseBody.content
    }
    
    return null
}

/**
 * Procesa respuestas que no tienen el campo "content" (GeneralResponse simple del backend).
 */
suspend fun HttpResponse.processBase(): Boolean {
    val responseBody = try {
        this.body<BaseResponse>()
    } catch (e: Exception) {
        null
    }

    if (responseBody != null) {
        handleBaseResponse(responseBody.success, responseBody.showAlert, responseBody.titleMessage, responseBody.textMessage)
        return responseBody.success
    }
    
    return false
}

fun handleBaseResponse(success: Boolean, showAlert: Boolean, title: String, text: String) {
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
}

object ApiConfig {
    val baseUrl = BuildValues.BACKEND_URL
}
