package com.ring.ring.todo.infra.network.request

import kotlinx.serialization.Serializable

@Serializable
data class GetRequest(
    val todoId: Long,
)