package com.example.cicloud.network

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
