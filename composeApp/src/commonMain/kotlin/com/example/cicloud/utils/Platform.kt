package com.example.cicloud.utils

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform