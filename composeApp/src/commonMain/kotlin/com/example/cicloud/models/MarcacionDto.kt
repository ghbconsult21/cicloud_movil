package com.example.cicloud.models

import kotlinx.serialization.Serializable

@Serializable
data class MarcacionDto(
    val id: Long,
    val horarioCodigo: String? = null,
    val fechaInicio: String? = null,
    val fechaFin: String? = null,
    val horaRegistroEntrada: String? = null,
    val horaRegistroSalida: String? = null,
    val horaAperturaInicio: String? = null,
    val horaAperturaFin: String? = null,
    val horaCierreInicio: String? = null,
    val horaCierreFin: String? = null,
    val conceptoAsistencia: String? = null,
    val registraIngreso: Boolean,
    val registraSalida: Boolean
)
