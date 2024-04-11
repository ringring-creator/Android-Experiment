package com.ring.ring.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitTodoNetworkApi {
    @POST("todo/list")
    suspend fun list(
        @Body request: ListRequest,
        @Header("Authorization") authorization: String,
    ): ListResponse

}
