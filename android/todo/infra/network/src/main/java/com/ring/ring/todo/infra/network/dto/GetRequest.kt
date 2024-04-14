package com.ring.ring.todo.infra.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetRequest(
    val todoId: Long,
)