package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.network.request.CreateRequest
import com.ring.ring.todo.infra.network.request.DeleteRequest
import com.ring.ring.todo.infra.network.request.EditDoneRequest
import com.ring.ring.todo.infra.network.request.EditRequest
import com.ring.ring.todo.infra.network.request.GetRequest
import com.ring.ring.todo.infra.network.response.GetResponse
import com.ring.ring.todo.infra.network.response.ListResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitTodoNetworkApi {
    @POST("todo/list")
    suspend fun list(
        @Header("Authorization") authorization: String,
    ): ListResponse

    @POST("todo/get")
    suspend fun get(
        @Body request: GetRequest,
        @Header("Authorization") authorization: String,
    ): GetResponse

    @POST("todo/create")
    suspend fun create(
        @Body request: CreateRequest,
        @Header("Authorization") authorization: String,
    )

    @POST("todo/edit")
    suspend fun edit(
        @Body request: EditRequest,
        @Header("Authorization") authorization: String,
    )

    @POST("todo/delete")
    suspend fun delete(
        @Body request: DeleteRequest,
        @Header("Authorization") authorization: String,
    )

    @POST("todo/edit-done")
    suspend fun editDone(
        @Body request: EditDoneRequest,
        @Header("Authorization") authorization: String,
    )
}
