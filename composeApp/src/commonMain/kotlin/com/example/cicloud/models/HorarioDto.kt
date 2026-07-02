package com.example.cicloud.models

import kotlinx.serialization.Serializable

@Serializable
data class HorarioRequest(
    val anio: Int,
    val mes: Int
)

/**
 * Modelo minimalista para el listado del calendario.
 */
@Serializable
data class HorarioDiaDto(
    val fecha: String, // "YYYY-MM-DD"
    val items: List<HorarioItemDto>
)

@Serializable
data class HorarioItemDto(
    val id: Int,
    val cod: String,
    val bg: String,
    val tx: String
)

/**
 * Modelo detallado para el modal.
 */
@Serializable
data class HorarioDetalleDto(
    val id: Int,
    val horario: String,
    val rangoHoras: String,
    val lugar: String,
    val tardanza: String? = null,
    val evaluacion: String? = null,
    val observacion: String? = null,
    val estado: String? = null
)
