package com.ring.ring.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface RetrofitUserNetworkApi {
    @Headers("API-Version: 1")
    @PUT("/users")
    suspend fun edit(
        @Body request: EditRequest,
        @Header("Authorization") authorization: String,
    )

    @Headers("API-Version: 1")
    @DELETE("/users")
    suspend fun withdrawal(
        @Header("Authorization") authorization: String,
    )

    @Headers("API-Version: 1")
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @Headers("API-Version: 1")
    @POST("signup")
    suspend fun signUp(@Body request: SignUpRequest)
}
