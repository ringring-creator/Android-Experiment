package com.ring.ring.network

import retrofit2.http.Body
import retrofit2.http.POST


interface RetrofitNetworkApi {
    @POST("user/login")
    suspend fun login(
        @Body user: LoginRequest,
    ): LoginResponse
}
