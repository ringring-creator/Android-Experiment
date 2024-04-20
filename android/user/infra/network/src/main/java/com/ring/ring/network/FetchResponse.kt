package com.ring.ring.network

import com.ring.ring.user.infra.model.User
import kotlinx.serialization.Serializable

@Serializable
data class FetchResponse(
    val userId: Long,
    val email: String,
    val token: String,
) {
    fun toUser(): User {
        return User.generate(userId, email, token)
    }
}