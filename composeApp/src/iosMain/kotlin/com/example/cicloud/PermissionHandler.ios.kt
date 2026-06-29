package com.example.cicloud

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun LocationPermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    LaunchedEffect(Unit) {
        // En iOS la solicitud de permisos se dispara automáticamente al usar CLLocationManager
        // Por ahora simulamos éxito
        onPermissionGranted()
    }
}
