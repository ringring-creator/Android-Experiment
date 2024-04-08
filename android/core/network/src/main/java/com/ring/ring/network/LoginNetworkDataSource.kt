package com.ring.ring.network

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val user: Account,
) {
    @Serializable
    data class Account(
        val email: String,
        val password: String,
    )
}

@Serializable
data class LoginResponse(
    val userId: Long,
    val token: String,
)

interface LoginNetworkDataSource {
    suspend fun login(request: LoginRequest): LoginResponse
}