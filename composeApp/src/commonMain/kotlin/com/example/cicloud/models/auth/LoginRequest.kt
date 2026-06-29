package com.example.cicloud.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val userName: String,
    val password: String,
    val identificadorCelular: String = "",
    val modeloCelular: String = "",
    val marcaCelular: String = ""
)
