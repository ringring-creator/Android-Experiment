package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.network.request.CreateRequest
import com.ring.ring.todo.infra.network.request.DeleteRequest
import com.ring.ring.todo.infra.network.request.EditDoneRequest
import com.ring.ring.todo.infra.network.request.EditRequest
import com.ring.ring.todo.infra.network.request.GetRequest
import com.ring.ring.todo.infra.network.response.GetResponse
import com.ring.ring.todo.infra.network.response.ListResponse
import javax.inject.Inject

class TodoRetrofitDataSource @Inject constructor(
    private val networkApi: RetrofitTodoNetworkApi,
) : TodoNetworkDataSource {
    override suspend fun list(token: String): ListResponse {
        return networkApi.list("Bearer $token")
    }

    override suspend fun get(request: GetRequest, token: String): GetResponse {
        return networkApi.get(request, "Bearer $token")
    }

    override suspend fun create(request: CreateRequest, token: String) {
        networkApi.create(request, "Bearer $token")
    }

    override suspend fun edit(request: EditRequest, token: String) {
        networkApi.edit(request, "Bearer $token")
    }

    override suspend fun delete(request: DeleteRequest, token: String) {
        networkApi.delete(request, "Bearer $token")
    }

    override suspend fun editDone(request: EditDoneRequest, token: String) {
        networkApi.editDone(request, "Bearer $token")
    }
}