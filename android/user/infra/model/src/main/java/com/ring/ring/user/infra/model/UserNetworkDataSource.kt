package com.ring.ring.user.infra.model

interface UserNetworkDataSource {
    suspend fun edit(credentials: Credentials)
    suspend fun withdrawal()
    suspend fun login(credentials: Credentials): User
    suspend fun signUp(credentials: Credentials)
}