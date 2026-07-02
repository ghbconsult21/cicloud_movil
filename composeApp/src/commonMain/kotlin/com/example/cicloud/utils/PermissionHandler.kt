package com.example.cicloud.utils

import androidx.compose.runtime.Composable

@Composable
expect fun LocationPermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
)

interface PermissionController {
    fun requestLocationPermission()
}
