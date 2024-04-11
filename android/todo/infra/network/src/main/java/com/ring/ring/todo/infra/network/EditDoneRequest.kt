package com.ring.ring.todo.infra.network

import kotlinx.serialization.Serializable

@Serializable
data class EditDoneRequest(
    val todoId: Long,
    val done: Boolean,
)