package com.example.cicloud.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cicloud.GlobalMessageManager
import com.example.cicloud.models.InstitucionDto
import com.example.cicloud.models.auth.LoginRequest
import com.example.cicloud.network.SessionManager
import com.example.cicloud.repository.AuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class LoginUiState(
    val instituciones: List<InstitucionDto> = emptyList(),
    val labelText: String = "Cargando instituciones...",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    
    var uiState by mutableStateOf(LoginUiState())
        private set

    private var searchJob: Job? = null

    init {
        loadInstituciones()
    }

    /**
     * Implementa debounce para buscar instituciones solo cuando el usuario deja de escribir.
     */
    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500.milliseconds) // Espera 500ms tras el último cambio
            loadInstituciones(query)
        }
    }

    private fun loadInstituciones(query: String? = null) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val res = repository.getInstituciones(query)
                if (res != null) {
                    uiState = uiState.copy(
                        instituciones = res,
                        labelText = if (query.isNullOrBlank()) "Seleccione la institución" else "Buscando: $query",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    labelText = "Error al cargar datos",
                    isLoading = false
                )
            }
        }
    }

    fun login(usuario: String, clave: String) {
        viewModelScope.launch {
            // Reiniciamos el estado de éxito y activamos carga
            uiState = uiState.copy(isLoading = true, loginSuccess = false)
            try {
                val request = LoginRequest(userName = usuario, password = clave)
                val response = repository.login(request)
                
                if (response != null) {
                    // Guardamos el token en el SessionManager
                    SessionManager.token = response.token
                    uiState = uiState.copy(isLoading = false, loginSuccess = true)
                } else {
                    uiState = uiState.copy(isLoading = false)
                    // Si no hubo excepción pero la respuesta es nula, informamos
                    if (!GlobalMessageManager.showAlert) {
                        GlobalMessageManager.show("Error", "No se pudo procesar la respuesta del servidor", "error")
                    }
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, loginSuccess = false)
                // Si el Interceptor/process() no mostró ya una alerta, la mostramos nosotros
                if (!GlobalMessageManager.showAlert) {
                    GlobalMessageManager.show(
                        "Credenciales Incorrectas", 
                        e.message ?: "Por favor, verifique su usuario y contraseña", 
                        "error"
                    )
                }
            }
        }
    }
}
