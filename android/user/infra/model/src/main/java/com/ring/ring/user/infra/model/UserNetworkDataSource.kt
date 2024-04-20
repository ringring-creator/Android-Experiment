package com.ring.ring.user.infra.model

interface UserNetworkDataSource {
    suspend fun fetch(): User
    suspend fun edit(credentials: Credentials)
    suspend fun withdrawal()
    suspend fun login(credentials: Credentials): User
    suspend fun signUp(credentials: Credentials)
}