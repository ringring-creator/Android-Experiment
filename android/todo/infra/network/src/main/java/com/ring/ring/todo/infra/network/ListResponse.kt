package com.ring.ring.todo.infra.network

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ListResponse(
    val todoList: List<Todo>,
) {
    @Serializable
    data class Todo(
        val id: Long,
        val title: String,
        val description: String,
        val done: Boolean,
        @Serializable(with = InstantSerializer::class)
        val deadline: Instant,
        val userId: Long,
    )
}