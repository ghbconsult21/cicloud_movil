package com.example.cicloud.repository

import com.example.cicloud.Endpoints
import com.example.cicloud.models.InstitucionDto
import com.example.cicloud.models.auth.LoginRequest
import com.example.cicloud.models.auth.LoginResponseDto
import com.example.cicloud.network.process
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthRepository(private val client: HttpClient) {
    
    suspend fun getInstituciones(query: String? = null): List<InstitucionDto>? {
        return client.get(Endpoints.getUrl(Endpoints.INSTITUCION_AUTOCOMPLETE)) {
            if (!query.isNullOrBlank()) {
                parameter("term", query)
            }
        }.process<List<InstitucionDto>>()
    }

    suspend fun login(request: LoginRequest): LoginResponseDto? {
        return client.post(Endpoints.getUrl(Endpoints.AUTH_MOBILE)) {
            setBody(request)
        }.process<LoginResponseDto>()
    }
}
