package com.ring.ring.user.infra.model

data class User(
    val id: Id,
    val email: Email,
    val token: String,
) {
    companion object {
        fun generate(id: Long, email: String, token: String): User = User(
            id = Id(id),
            email = Email(email),
            token = token,
        )
    }
}
