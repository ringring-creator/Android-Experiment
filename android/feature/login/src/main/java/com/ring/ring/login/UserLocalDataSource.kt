package com.ring.ring.login

import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun save(user: User)
    suspend fun getUser(): Flow<User?>
}