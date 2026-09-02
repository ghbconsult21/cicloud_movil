package com.example.cicloud.models

import kotlinx.serialization.Serializable

@Serializable
data class VersionResponse(
    val version: String,
    val code: String,
    val downloadUrl: String? = null
)
