package com.ring.ring.todo.infra.network.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteRequest(
    val todoId: Long,
)