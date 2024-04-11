package com.ring.ring.network

interface TodoNetworkDataSource {
    suspend fun list(request: ListRequest, token: String): ListResponse
}