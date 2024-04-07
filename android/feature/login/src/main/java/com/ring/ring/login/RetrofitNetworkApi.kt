package com.ring.ring.login

import retrofit2.http.POST
import retrofit2.http.Query


interface RetrofitNetworkApi {
    @POST("user/login")
    suspend fun login(
        @Query("user") user: LoginRequest,
    ): LoginResponse
}
