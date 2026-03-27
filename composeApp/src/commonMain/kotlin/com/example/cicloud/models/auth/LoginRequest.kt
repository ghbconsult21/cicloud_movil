package com.example.cicloud.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val userName: String,
    val password: String
)
