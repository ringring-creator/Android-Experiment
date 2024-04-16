package com.ring.ring.user.infra.model

data class Password(
    val value: String
) {
    companion object {
        fun isInvalidPassword(password: String): Boolean {
            if (password.length < 8) return true

            var hasDigit = false
            var hasUpperCase = false
            var hasLowerCase = false

            for (char in password) {
                when {
                    char.isDigit() -> hasDigit = true
                    char.isUpperCase() -> hasUpperCase = true
                    char.isLowerCase() -> hasLowerCase = true
                }
                if (hasDigit && hasUpperCase && hasLowerCase) return false
            }
            return true
        }
    }
}