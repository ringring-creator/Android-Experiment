package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.network.dto.CreateRequest
import com.ring.ring.todo.infra.network.dto.EditDoneRequest
import com.ring.ring.todo.infra.network.dto.ListRequest
import com.ring.ring.todo.infra.network.dto.ListResponse

interface TodoNetworkDataSource {
    suspend fun list(request: ListRequest, token: String): ListResponse
    suspend fun create(request: CreateRequest, token: String)
    suspend fun editDone(request: EditDoneRequest, token: String)
}