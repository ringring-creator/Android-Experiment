package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.network.dto.CreateRequest
import com.ring.ring.todo.infra.network.dto.EditDoneRequest
import com.ring.ring.todo.infra.network.dto.ListRequest
import com.ring.ring.todo.infra.network.dto.ListResponse
import javax.inject.Inject

class TodoRetrofitDataSource @Inject constructor(
    private val networkApi: RetrofitTodoNetworkApi,
) : TodoNetworkDataSource {
    override suspend fun list(request: ListRequest, token: String): ListResponse {
        return networkApi.list(request, "Bearer $token")
    }

    override suspend fun create(request: CreateRequest, token: String) {
        networkApi.create(request, "Bearer $token")
    }

    override suspend fun editDone(request: EditDoneRequest, token: String) {
        networkApi.editDone(request, "Bearer $token")
    }
}