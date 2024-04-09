package com.ring.ring.network

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val credentials: Credentials,
) {
    @Serializable
    data class Credentials(
        val email: String,
        val password: String,
    )
}