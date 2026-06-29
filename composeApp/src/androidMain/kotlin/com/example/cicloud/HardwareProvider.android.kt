package com.example.cicloud

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.annotation.SuppressLint
import android.provider.Settings
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object AndroidHardwareManager {
    lateinit var context: Context
}

actual object HardwareProvider {
    actual fun getDeviceId(): String {
        return Settings.Secure.getString(
            AndroidHardwareManager.context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown_android"
    }

    actual fun hasLocationPermission(): Boolean {
        val context = AndroidHardwareManager.context
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
               ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocation(): LocationInfo? {
        val context = AndroidHardwareManager.context
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!hasLocationPermission()) {
            return null
        }

        return suspendCancellableCoroutine { continuation ->
            try {
                val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) 
                    ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                
                if (location != null) {
                    continuation.resume(object : LocationInfo {
                        override val latitude: Double = location.latitude
                        override val longitude: Double = location.longitude
                    })
                } else {
                    continuation.resume(null)
                }
            } catch (e: Exception) {
                continuation.resume(null)
            }
        }
    }
}
