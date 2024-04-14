package com.ring.ring.todo.infra.network.dto

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.network.InstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TodoModel(
    val id: Long?,
    val title: String,
    val description: String,
    val done: Boolean,
    @Serializable(with = InstantSerializer::class)
    val deadline: Instant,
) {
    fun toTodo(): Todo = Todo(
        id = id,
        title = title,
        description = description,
        done = done,
        deadline = deadline,
    )
}