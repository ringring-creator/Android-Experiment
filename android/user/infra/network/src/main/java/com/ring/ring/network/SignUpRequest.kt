package com.ring.ring.network

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val credentials: CredentialsModel,
)