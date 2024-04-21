package com.ring.ring.user.infra.model

data class User(
    val id: Id,
    val email: Email,
    val password: Password,
    val token: String,
) {
    companion object {
        fun generate(
            id: Long,
            email: String,
            password: String,
            token: String
        ): User = User(
            id = Id(id),
            email = Email(email),
            password = Password(password),
            token = token,
        )

        fun generate(
            id: Long,
            token: String,
            credentials: Credentials
        ): User = User(
            id = Id(id),
            email = credentials.email,
            password = credentials.password,
            token = token,
        )
    }
}
