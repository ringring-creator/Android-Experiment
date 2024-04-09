package com.ring.ring.network

interface LoginNetworkDataSource {
    suspend fun login(request: LoginRequest): LoginResponse
}