package com.ring.ring.user.infra.model

data class Credentials(
    val email: Email,
    val password: Password,
) {
    companion object {
        fun issue(
            email: String,
            password: String,
        ): Credentials = Credentials(
            email = Email(email),
            password = Password(password),
        )
    }
}
