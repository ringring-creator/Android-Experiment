package com.ring.ring.todo.infra.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class EditDoneRequest(
    val todoId: Long,
    val done: Boolean,
)