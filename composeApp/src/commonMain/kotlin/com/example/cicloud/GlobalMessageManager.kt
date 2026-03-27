package com.example.cicloud

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Gestor global de mensajes y alertas.
 * Soporta alertas simples y de confirmación (Aceptar/Cancelar).
 */
object GlobalMessageManager {
    var showAlert by mutableStateOf(false)
    var title by mutableStateOf("")
    var message by mutableStateOf("")
    var severity by mutableStateOf("info") // success, warn, error
    
    // Para alertas de confirmación
    var isConfirmation by mutableStateOf(false)
    var onConfirmAction: (() -> Unit)? = null
    var onCancelAction: (() -> Unit)? = null

    /**
     * Muestra una alerta simple con un botón de Aceptar.
     */
    fun show(title: String, message: String, severity: String = "info") {
        this.title = title
        this.message = message
        this.severity = severity
        this.isConfirmation = false
        this.onConfirmAction = null
        this.onCancelAction = null
        this.showAlert = true
    }

    /**
     * Muestra una alerta de confirmación con botones Aceptar y Cancelar.
     */
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
