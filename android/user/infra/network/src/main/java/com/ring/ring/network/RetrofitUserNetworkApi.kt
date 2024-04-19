package com.ring.ring.network

import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitUserNetworkApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("signup")
    suspend fun signUp(@Body request: SignUpRequest)
}
