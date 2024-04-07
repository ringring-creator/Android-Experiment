package com.ring.ring.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class LoginResponse(
    val userId: Long,
    val token: String,
)

interface LoginNetworkDataSource {
    suspend fun login(request: LoginRequest): LoginResponse
}