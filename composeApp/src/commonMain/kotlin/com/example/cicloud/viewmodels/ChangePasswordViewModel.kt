package com.example.cicloud.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cicloud.GlobalMessageManager
import com.example.cicloud.models.auth.ChangePasswordRequest
import com.example.cicloud.repository.AuthRepository
import kotlinx.coroutines.launch

data class ChangePasswordUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class ChangePasswordViewModel(private val repository: AuthRepository) : ViewModel() {

    var uiState by mutableStateOf(ChangePasswordUiState())
        private set

    fun clearState() {
        uiState = ChangePasswordUiState()
    }

    fun cambiarPassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        if (oldPassword.isBlank()) {
            GlobalMessageManager.show("Alerta", "Ingrese su contraseña actual", "warn")
            return
        }
        if (newPassword.isBlank()) {
            GlobalMessageManager.show("Alerta", "Ingrese la nueva contraseña", "warn")
            return
        }
        if (newPassword.length < 6) {
            GlobalMessageManager.show("Alerta", "La nueva contraseña debe tener al menos 6 caracteres", "warn")
            return
        }
        if (newPassword != confirmPassword) {
            GlobalMessageManager.show("Alerta", "Las contraseñas no coinciden", "warn")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null, isSuccess = false)
            try {
                val request = ChangePasswordRequest(
                    oldPasswod = oldPassword,
                    newPasswod = newPassword
                )
                val success = repository.cambiarPassword(request)
                if (success) {
                    uiState = uiState.copy(isLoading = false, isSuccess = true)
                    onSuccess()
                } else {
                    uiState = uiState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = e.message)
                // Si GlobalMessageManager no mostró alerta, mostrarla aquí
                if (!GlobalMessageManager.showAlert) {
                    GlobalMessageManager.show("Error", e.message ?: "No se pudo cambiar la contraseña", "error")
                }
            }
        }
    }
}
