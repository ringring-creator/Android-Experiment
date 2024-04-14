package com.ring.ring.todo.infra.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetResponse(
    val todo: TodoModel
)