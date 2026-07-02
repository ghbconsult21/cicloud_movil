package com.example.cicloud.repository

import com.example.cicloud.network.Endpoints
import com.example.cicloud.models.MarcacionDto
import com.example.cicloud.models.RegistrarMarcacionRequest
import com.example.cicloud.models.HorarioDiaDto
import com.example.cicloud.models.HorarioRequest
import com.example.cicloud.network.process
import com.example.cicloud.network.processBase
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class AsistenciaRepository(private val client: HttpClient) {
    suspend fun getMarcaciones(): List<MarcacionDto>? {
        return client.get(Endpoints.getUrl(Endpoints.ASISTENCIA_LISTAR))
            .process<List<MarcacionDto>>()
    }

    suspend fun registrarMarcacion(request: RegistrarMarcacionRequest): Boolean {
        return try {
            client.post(Endpoints.getUrl(Endpoints.ASISTENCIA_REGISTRAR)) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.processBase()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getMiHorario(year: Int, month: Int): List<HorarioDiaDto>? {
        return client.post(Endpoints.getUrl(Endpoints.ASISTENCIA_HORARIO)) {
            contentType(ContentType.Application.Json)
            setBody(HorarioRequest(anio = year, mes = month))
        }.process<List<HorarioDiaDto>>()
    }
}
