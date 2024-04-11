package com.ring.ring.todo.infra.local

import com.ring.ring.infra.db.TodoEntity
import kotlinx.datetime.Instant

data class LocalTodo(
    val id: Long?,
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: Instant,
) {
    fun toTodoEntity() = TodoEntity(
        id = id,
        title = title,
        description = description,
        done = done,
        deadline = deadline,
    )
}

fun TodoEntity.toLocalTodo() = LocalTodo(
    id = id,
    title = title,
    description = description,
    done = done,
    deadline = deadline,
)