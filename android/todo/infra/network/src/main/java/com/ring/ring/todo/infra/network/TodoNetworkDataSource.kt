package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.network.request.CreateRequest
import com.ring.ring.todo.infra.network.request.DeleteRequest
import com.ring.ring.todo.infra.network.request.EditDoneRequest
import com.ring.ring.todo.infra.network.request.EditRequest
import com.ring.ring.todo.infra.network.request.GetRequest
import com.ring.ring.todo.infra.network.response.GetResponse
import com.ring.ring.todo.infra.network.response.ListResponse

interface TodoNetworkDataSource {
    suspend fun list(token: String): ListResponse
    suspend fun get(request: GetRequest, token: String): GetResponse
    suspend fun create(request: CreateRequest, token: String)
    suspend fun edit(request: EditRequest, token: String)
    suspend fun editDone(request: EditDoneRequest, token: String)
    suspend fun delete(request: DeleteRequest, token: String)
}