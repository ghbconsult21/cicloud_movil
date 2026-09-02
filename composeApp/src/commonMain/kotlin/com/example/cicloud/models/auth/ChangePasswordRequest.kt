package com.example.cicloud.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val oldPasswod: String,
    val newPasswod: String
)
