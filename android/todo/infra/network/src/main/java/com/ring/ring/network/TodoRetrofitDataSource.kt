package com.ring.ring.network

import javax.inject.Inject

class TodoRetrofitDataSource @Inject constructor(
    private val networkApi: RetrofitTodoNetworkApi
) : TodoNetworkDataSource {
    override suspend fun list(request: ListRequest, token: String): ListResponse {
        return networkApi.list(request, "Bearer $token")
    }
}