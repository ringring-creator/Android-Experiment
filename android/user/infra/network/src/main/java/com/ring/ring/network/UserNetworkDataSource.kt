package com.ring.ring.network

interface UserNetworkDataSource {
    suspend fun login(request: LoginRequest): LoginResponse
    suspend fun signUp(request: SignUpRequest)
}