package com.example.cicloud.models

import kotlinx.serialization.Serializable

@Serializable
data class InstitucionDto(
    val id: Int,
    val text: String
)
