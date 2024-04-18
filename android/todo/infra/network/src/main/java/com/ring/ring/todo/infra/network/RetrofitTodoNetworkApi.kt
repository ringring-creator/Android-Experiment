package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.network.dto.CreateRequest
import com.ring.ring.todo.infra.network.dto.EditDoneRequest
import com.ring.ring.todo.infra.network.dto.EditRequest
import com.ring.ring.todo.infra.network.dto.GetResponse
import com.ring.ring.todo.infra.network.dto.ListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitTodoNetworkApi {
    @GET("todo/list")
    suspend fun list(
        @Header("Authorization") authorization: String,
    ): ListResponse

    @GET("todo/get/{todoId}")
    suspend fun get(
        @Path("todoId") todoId: Long,
        @Header("Authorization") authorization: String,
    ): GetResponse

    @POST("todo/create")
    suspend fun create(
        @Body request: CreateRequest,
        @Header("Authorization") authorization: String,
    )

    @PUT("todo/edit")
    suspend fun edit(
        @Body request: EditRequest,
        @Header("Authorization") authorization: String,
    )

    @DELETE("todo/delete/{todoId}")
    suspend fun delete(
        @Path("todoId") todoId: Long,
        @Header("Authorization") authorization: String,
    )

    @PATCH("todo/edit-done")
    suspend fun editDone(
        @Body request: EditDoneRequest,
        @Header("Authorization") authorization: String,
    )
}
