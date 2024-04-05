package com.ring.ring.usecase.user

import java.security.MessageDigest

object Cipher {
    fun hashWithSHA256(input: String): String {
        val bytes = input.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") {
            String.format("%02x", it)
        }
    }
}