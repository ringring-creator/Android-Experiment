package com.ring.ring.network

import com.ring.ring.user.infra.model.Credentials
import com.ring.ring.user.infra.model.User
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userId: Long,
    val token: String,
) {
    fun toUser(credentials: Credentials): User {
        return User.generate(userId, token, credentials)
    }
}