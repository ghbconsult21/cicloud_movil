package com.example.cicloud

interface LocationInfo {
    val latitude: Double
    val longitude: Double
}

expect object HardwareProvider {
    fun getDeviceId(): String
    suspend fun getCurrentLocation(): LocationInfo?
}
