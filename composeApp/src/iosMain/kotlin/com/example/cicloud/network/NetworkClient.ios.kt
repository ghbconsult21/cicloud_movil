package com.example.cicloud.network

import io.ktor.client.*

actual fun HttpClientConfig<*>.configurePlatformEngine() {
    // No bypass necesario en iOS por ahora, o implementar según motor (Darwin)
}
