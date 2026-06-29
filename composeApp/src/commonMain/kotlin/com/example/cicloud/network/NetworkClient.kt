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
import kotlin.time.TimeSource
import kotlin.time.Duration.Companion.hours

object SessionManager {
    var token: String? = null
    var institucionId: Int = 0
    var userName by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    
    // Usamos TimeMark para medir inactividad de forma precisa
    private var lastActivityMark: TimeSource.Monotonic.ValueTimeMark? = null
    
    fun resetSession() {
        token = null
        institucionId = 0
        userName = ""
        isLoggedIn = false
        lastActivityMark = null
    }

    fun updateActivity() {
        if (isLoggedIn) {
            lastActivityMark = TimeSource.Monotonic.markNow()
        }
    }

    fun checkInactivity(onLogout: () -> Unit) {
        val mark = lastActivityMark
        if (isLoggedIn && mark != null) {
            // Si el tiempo transcurrido es mayor a 1 hora
            if (mark.elapsedNow() > 1.hours) {
                resetSession()
                onLogout()
            }
        }
    }
}

/**
 * Cliente HTTP configurado para ser dinámico.
 */
val httpClient = HttpClient {
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
            GlobalMessageManager.show("SIN CONEXIÓN", "No podemos conectarnos al servicio", "error")
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
