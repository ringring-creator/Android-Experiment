package com.ring.ring.todo.infra.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitTodoNetworkApi {
    @POST("todo/list")
    suspend fun list(
        @Body request: ListRequest,
        @Header("Authorization") authorization: String,
    ): ListResponse

    @POST("todo/editdone")
    suspend fun editDone(
        @Body request: EditDoneRequest,
        @Header("Authorization") authorization: String,
    )
}
