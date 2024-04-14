package com.ring.ring.todo.infra.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class EditRequest(
    val todo: TodoModel,
)