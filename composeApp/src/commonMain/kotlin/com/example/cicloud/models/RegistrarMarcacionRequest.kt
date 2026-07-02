package com.example.cicloud.models

import kotlinx.serialization.Serializable

@Serializable
data class RegistrarMarcacionRequest(
    val id: Long,
    val proceso: Int,
    val latitude: String,
    val longitude: String,
    val idCelular: String
)
