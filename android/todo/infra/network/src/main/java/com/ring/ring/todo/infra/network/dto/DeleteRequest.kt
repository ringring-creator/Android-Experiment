package com.ring.ring.todo.infra.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteRequest(
    val todoId: Long,
)