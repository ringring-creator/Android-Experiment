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