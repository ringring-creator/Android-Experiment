package com.ring.ring.login

interface UserLocalDataSource {
    suspend fun save(user: User)
    suspend fun getUser(): User?
}