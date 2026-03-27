package com.example.cicloud.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val token: String,
    val userId: String,
    val personId: Int,
    val rolId: String,
    val changedPassword: Boolean
)
