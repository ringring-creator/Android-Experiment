package com.ring.ring.user.infra.model

interface UserLocalDataSource {
    suspend fun save(user: User)
    suspend fun getUser(): User?
}