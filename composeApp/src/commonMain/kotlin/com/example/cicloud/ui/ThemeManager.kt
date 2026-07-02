package com.example.cicloud.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeManager {
    var isDarkMode by mutableStateOf(false)

    fun toggleTheme() {
        isDarkMode = !isDarkMode
    }
}
