package com.ring.ring.login

interface UserRepository {
    suspend fun login(email: String, password: String)
}