package com.ring.ring.network

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val credentials: CredentialsModel,
)