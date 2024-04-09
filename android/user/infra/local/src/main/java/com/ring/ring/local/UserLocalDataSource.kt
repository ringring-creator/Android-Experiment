package com.ring.ring.local

interface UserLocalDataSource {
    suspend fun save(user: User)
    suspend fun getUser(): User?
}