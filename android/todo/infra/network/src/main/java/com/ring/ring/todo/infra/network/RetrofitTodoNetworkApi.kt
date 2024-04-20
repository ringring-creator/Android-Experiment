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
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitTodoNetworkApi {
    @Headers("API-Version: 1")
    @GET("todos")
    suspend fun list(
        @Header("Authorization") authorization: String,
    ): ListResponse

    @Headers("API-Version: 1")
    @GET("todos/{todoId}")
    suspend fun get(
        @Path("todoId") todoId: Long,
        @Header("Authorization") authorization: String,
    ): GetResponse

    @Headers("API-Version: 1")
    @POST("todos")
    suspend fun create(
        @Body request: CreateRequest,
        @Header("Authorization") authorization: String,
    )

    @Headers("API-Version: 1")
    @PUT("todos/{todoId}")
    suspend fun edit(
        @Path("todoId") todoId: Long,
        @Body request: EditRequest,
        @Header("Authorization") authorization: String,
    )

    @Headers("API-Version: 1")
    @DELETE("todos/{todoId}")
    suspend fun delete(
        @Path("todoId") todoId: Long,
        @Header("Authorization") authorization: String,
    )

    @Headers("API-Version: 1")
    @PATCH("todos/edit-done/{todoId}")
    suspend fun editDone(
        @Path("todoId") todoId: Long,
        @Body request: EditDoneRequest,
        @Header("Authorization") authorization: String,
    )
}
