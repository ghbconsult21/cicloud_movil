package com.example.cicloud

import platform.UIKit.UIDevice

actual object HardwareProvider {
    actual fun getDeviceId(): String {
        return UIDevice.currentDevice.identifierForVendor?.UUIDString ?: "unknown_ios"
    }

    actual fun getBrand(): String = "Apple"
    actual fun getModel(): String = UIDevice.currentDevice.model

    actual fun hasLocationPermission(): Boolean {
        // En iOS esto suele manejarse de forma más compleja con CLLocationManager
        // Por ahora retornamos true para no bloquear el flujo
        return true
    }

    actual suspend fun getCurrentLocation(): LocationInfo? {
        // Placeholder para iOS
        return object : LocationInfo {
            override val latitude: Double = -12.046374
            override val longitude: Double = -77.042793
        }
    }
}
