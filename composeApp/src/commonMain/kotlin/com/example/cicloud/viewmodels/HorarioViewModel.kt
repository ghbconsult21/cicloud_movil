package com.example.cicloud.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cicloud.models.HorarioDiaDto
import com.example.cicloud.repository.AsistenciaRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.Month

data class HorarioUiState(
    val horarios: List<HorarioDiaDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HorarioViewModel(private val repository: AsistenciaRepository) : ViewModel() {
    var uiState by mutableStateOf(HorarioUiState())
        private set

    fun loadHorarios(year: Int, month: Month) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                // month.ordinal es 0-11, sumamos 1 para obtener 1-12
                val monthNumber = month.ordinal + 1
                val result = repository.getMiHorario(year, monthNumber)
                uiState = uiState.copy(
                    horarios = result ?: emptyList(),
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar horarios"
                )
            }
        }
    }
}
