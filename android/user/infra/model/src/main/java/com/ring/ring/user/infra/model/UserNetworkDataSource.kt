package com.ring.ring.user.infra.model

interface UserNetworkDataSource {
    suspend fun edit(credentials: Credentials, token: String)
    suspend fun withdrawal(token: String)
    suspend fun login(credentials: Credentials): User
    suspend fun signUp(credentials: Credentials)
}