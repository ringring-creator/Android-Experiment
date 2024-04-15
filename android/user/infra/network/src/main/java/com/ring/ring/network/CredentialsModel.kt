package com.ring.ring.network

import kotlinx.serialization.Serializable

@Serializable
data class CredentialsModel(
    val email: String,
    val password: String,
)