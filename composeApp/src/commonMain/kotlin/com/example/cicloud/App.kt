package com.example.cicloud

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.cicloud.di.appModule
import com.example.cicloud.ui.MainScaffold
import com.example.cicloud.ui.ThemeManager
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        // Usamos nuestro tema personalizado que ya incluye los ColorConstants
        CicloudTheme(darkTheme = ThemeManager.isDarkMode) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainScaffold()
            }
        }
    }
}
