package com.example.cicloud

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Gestor global de mensajes y alertas con soporte para severidad y colores.
 */
object GlobalMessageManager {
    var showAlert by mutableStateOf(false)
    var title by mutableStateOf("")
    var message by mutableStateOf("")
    var severity by mutableStateOf("info") // success, warn, error, info
    
    // Para alertas de confirmación
    var isConfirmation by mutableStateOf(false)
    var onConfirmAction: (() -> Unit)? = null
    var onCancelAction: (() -> Unit)? = null

    /**
     * Retorna el color asociado a la severidad actual.
     */
    fun getSeverityColor(): Color {
        return when (severity) {
            "success" -> ColorConstants.Success
            "warn" -> ColorConstants.Warning
            "error" -> ColorConstants.Error
            else -> ColorConstants.Info
        }
    }

    /**
     * Retorna el icono asociado a la severidad actual.
     */
    fun getSeverityIcon(): ImageVector {
        return when (severity) {
            "success" -> Icons.Default.Check
            "warn" -> Icons.Default.Warning
            "error" -> Icons.Default.Warning // Usamos Warning pintado de rojo para Error
            else -> Icons.Default.Info
        }
    }

    fun show(title: String, message: String, severity: String = "info") {
        this.title = title
        this.message = message
        this.severity = severity
        this.isConfirmation = false
        this.onConfirmAction = null
        this.onCancelAction = null
        this.showAlert = true
    }

    fun showConfirm(
        title: String, 
        message: String, 
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = null
    ) {
        this.title = title
        this.message = message
        this.severity = "info"
        this.isConfirmation = true
        this.onConfirmAction = onConfirm
        this.onCancelAction = onCancel
        this.showAlert = true
    }

    fun dismiss() {
        showAlert = false
    }
}
