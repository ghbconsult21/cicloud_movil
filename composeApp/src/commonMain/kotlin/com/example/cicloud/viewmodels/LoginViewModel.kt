package com.example.cicloud.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cicloud.models.InstitucionDto
import com.example.cicloud.models.auth.LoginRequest
import com.example.cicloud.network.SessionManager
import com.example.cicloud.repository.AuthRepository
import kotlinx.coroutines.launch

data class LoginUiState(
    val instituciones: List<InstitucionDto> = emptyList(),
    val labelText: String = "Cargando instituciones...",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    
    var uiState by mutableStateOf(LoginUiState())
        private set

    init {
        loadInstituciones()
    }

    private fun loadInstituciones() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val res = repository.getInstituciones()
                if (res != null) {
                    uiState = uiState.copy(
                        instituciones = res,
                        labelText = "Seleccione la institución",
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
            uiState = uiState.copy(isLoading = true)
            try {
                val request = LoginRequest(userName = usuario, password = clave)
                val response = repository.login(request)
                
                if (response != null) {
                    // Guardamos el token en el SessionManager
                    SessionManager.token = response.token
                    uiState = uiState.copy(isLoading = false, loginSuccess = true)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false)
                // El error ya es manejado por el Interceptor (GlobalMessageManager)
            }
        }
    }
}
