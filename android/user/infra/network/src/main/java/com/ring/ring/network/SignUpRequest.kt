package com.ring.ring.network

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val credentials: Credentials,
) {
    @Serializable
    data class Credentials(
        val email: String,
        val password: String,
    )
}