package com.ring.ring.user.infra.local

data class LocalUser(
    val userId: Long,
    val email: String,
    val token: String,
)

interface UserLocalDataSource {
    suspend fun save(user: LocalUser)
    suspend fun getUser(): LocalUser?
}