package com.example.cicloud.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cicloud.GlobalMessageManager
import com.example.cicloud.HardwareProvider
import com.example.cicloud.models.MarcacionDto
import com.example.cicloud.models.RegistrarMarcacionRequest
import com.example.cicloud.repository.AsistenciaRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock

data class AsistenciaUiState(
    val marcaciones: List<MarcacionDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AsistenciaViewModel(private val repository: AsistenciaRepository) : ViewModel() {
    var uiState by mutableStateOf(AsistenciaUiState())
        private set

    init {
        loadMarcaciones()
    }

    fun loadMarcaciones() {
        viewModelScope.launch {
            refreshMarcaciones()
        }
    }

    private suspend fun refreshMarcaciones() {
        uiState = uiState.copy(isLoading = true, error = null)
        try {
            val result = repository.getMarcaciones()
            uiState = uiState.copy(
                marcaciones = result ?: emptyList(),
                isLoading = false
            )
        } catch (e: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                error = e.message ?: "Error desconocido"
            )
        }
    }

    fun registrarFuncion(proceso: Int, marcacion: MarcacionDto) {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        
        fun formatTime(dt: LocalDateTime): String {
            val h = dt.hour.toString().padStart(2, '0')
            val m = dt.minute.toString().padStart(2, '0')
            return "$h:$m"
        }

        try {
            val (tipoLabel, inicioStr, finStr) = when (proceso) {
                1 -> Triple("entrada", marcacion.horaAperturaInicio, marcacion.horaAperturaFin)
                2 -> Triple("salida", marcacion.horaCierreInicio, marcacion.horaCierreFin)
                else -> Triple(null, null, null)
            }

            if (tipoLabel != null && inicioStr != null && finStr != null) {
                val inicio = LocalDateTime.parse(inicioStr)
                val fin = LocalDateTime.parse(finStr)
                if (now < inicio || now > fin) {
                    GlobalMessageManager.show(
                        "Fuera de Rango",
                        "La $tipoLabel debe ser entre ${formatTime(inicio)} y ${formatTime(fin)} horas",
                        "warn"
                    )
                    return
                }
            }

            viewModelScope.launch {
                uiState = uiState.copy(isLoading = true)
                
                val location = HardwareProvider.getCurrentLocation()
                val deviceId = HardwareProvider.getDeviceId()
                
                val request = RegistrarMarcacionRequest(
                    id = marcacion.id,
                    proceso = proceso,
                    latitude = location?.latitude?.toString() ?: "",
                    longitude = location?.longitude?.toString() ?: "",
                    idCelular = deviceId
                )

                val success = repository.registrarMarcacion(request)
                if (success) {
                    // Refrescamos la lista esperando a que termine antes de ocultar el loading
                    refreshMarcaciones()
                } else {
                    uiState = uiState.copy(isLoading = false)
                }
            }
        } catch (e: Exception) {
            GlobalMessageManager.show("Error", "Error al procesar el registro: ${e.message}", "error")
        }
    }
}
