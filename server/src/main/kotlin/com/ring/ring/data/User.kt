package com.ring.ring.data

import io.ktor.server.auth.Principal

data class User(
    val id: Long?,
    val email: String,
    val password: String,
) : Principal