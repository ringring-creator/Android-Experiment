package com.ring.ring.data

import io.ktor.server.auth.Principal
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long?,
    val email: String,
    val password: String,
) : Principal