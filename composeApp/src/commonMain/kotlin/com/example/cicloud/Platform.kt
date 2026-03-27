package com.example.cicloud

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform