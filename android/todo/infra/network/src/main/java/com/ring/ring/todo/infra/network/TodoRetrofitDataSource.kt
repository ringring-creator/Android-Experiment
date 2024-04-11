package com.ring.ring.todo.infra.network

import javax.inject.Inject

class TodoRetrofitDataSource @Inject constructor(
    private val networkApi: RetrofitTodoNetworkApi
) : TodoNetworkDataSource {
    override suspend fun list(request: ListRequest, token: String): ListResponse {
        return networkApi.list(request, "Bearer $token")
    }

    override suspend fun editDone(request: EditDoneRequest, token: String) {
        return networkApi.editDone(request, "Bearer $token")
    }
}