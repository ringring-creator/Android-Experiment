package com.ring.ring.network

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userId: Long,
    val token: String,
)