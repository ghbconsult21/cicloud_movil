package com.example.cicloud.repository

import com.example.cicloud.Endpoints
import com.example.cicloud.models.MarcacionDto
import com.example.cicloud.models.RegistrarMarcacionRequest
import com.example.cicloud.network.process
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class AsistenciaRepository(private val client: HttpClient) {
    suspend fun getMarcaciones(): List<MarcacionDto>? {
        return client.get(Endpoints.getUrl(Endpoints.ASISTENCIA_LISTAR))
            .process<List<MarcacionDto>>()
    }

    suspend fun registrarMarcacion(request: RegistrarMarcacionRequest): Boolean {
        return client.post(Endpoints.getUrl(Endpoints.ASISTENCIA_REGISTRAR)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.process<String>() != null
    }
}
