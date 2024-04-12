package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.network.dto.CreateRequest
import com.ring.ring.todo.infra.network.dto.EditDoneRequest
import com.ring.ring.todo.infra.network.dto.ListRequest
import com.ring.ring.todo.infra.network.dto.ListResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitTodoNetworkApi {
    @POST("todo/list")
    suspend fun list(
        @Body request: ListRequest,
        @Header("Authorization") authorization: String,
    ): ListResponse

    @POST("todo/create")
    suspend fun create(
        @Body request: CreateRequest,
        @Header("Authorization") authorization: String,
    )

    @POST("todo/edit-done")
    suspend fun editDone(
        @Body request: EditDoneRequest,
        @Header("Authorization") authorization: String,
    )
}
