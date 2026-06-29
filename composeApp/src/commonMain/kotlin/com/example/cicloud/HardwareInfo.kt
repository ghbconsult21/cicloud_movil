package com.example.cicloud

interface LocationInfo {
    val latitude: Double
    val longitude: Double
}

expect object HardwareProvider {
    fun getDeviceId(): String
    fun getBrand(): String
    fun getModel(): String
    fun hasLocationPermission(): Boolean
    suspend fun getCurrentLocation(): LocationInfo?
}
