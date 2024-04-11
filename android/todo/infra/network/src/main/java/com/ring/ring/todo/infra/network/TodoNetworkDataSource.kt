package com.ring.ring.todo.infra.network

interface TodoNetworkDataSource {
    suspend fun list(request: ListRequest, token: String): ListResponse
    suspend fun editDone(request: EditDoneRequest, token: String)
}