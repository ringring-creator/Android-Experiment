package com.ring.ring.user.infra.model

interface UserNetworkDataSource {
    suspend fun login(credentials: Credentials): User
    suspend fun signUp(credentials: Credentials)
}