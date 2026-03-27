package com.example.cicloud.models

import kotlinx.serialization.Serializable

@Serializable
data class GeneralResponse<T>(
    val success: Boolean,
    val titleMessage: String = "",
    val textMessage: String = "",
    val showAlert: Boolean = false,
    val content: T? = null
)

@Serializable
data class BaseResponse(
    val success: Boolean,
    val titleMessage: String = "",
    val textMessage: String = "",
    val showAlert: Boolean = false
)
