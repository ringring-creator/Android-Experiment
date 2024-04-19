package com.ring.ring.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitUserNetworkApi {
    @Headers("API-Version: 1")
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @Headers("API-Version: 1")
    @POST("signup")
    suspend fun signUp(@Body request: SignUpRequest)
}
