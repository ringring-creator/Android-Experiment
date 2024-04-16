package com.ring.ring.user.infra.model

data class Email(
    val value: String,
) {
    init {
        if (isInvalidEmail(value)) throw IllegalArgumentException("Email is invalid")
    }

    companion object {
        fun isInvalidEmail(email: String): Boolean {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
            return email.matches(emailRegex).not()
        }
    }
}