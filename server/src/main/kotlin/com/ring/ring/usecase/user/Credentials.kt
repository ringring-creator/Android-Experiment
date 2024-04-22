package com.ring.ring.usecase.user

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    val email: String,
    val password: String,
)