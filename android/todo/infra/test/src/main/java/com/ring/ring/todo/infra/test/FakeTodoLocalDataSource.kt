package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import kotlinx.datetime.Instant

class FakeTodoLocalDataSource : TodoLocalDataSource {
    private var values: MutableList<Todo> = mutableListOf(
        Todo(
            1,
            "fakeTitle",
            "fakeDescription",
            false,
            Instant.parse("2024-01-01T00:00:00Z"),
        ),
        Todo(
            2,
            "fakeTitle2",
            "fakeDescription2",
            true,
            Instant.parse("2024-12-31T00:00:00Z"),
        ),
    )

    override suspend fun list(): List<Todo> {
        return values
    }

    override suspend fun upsert(todoList: List<Todo>) {
        values.addAll(todoList)
    }

    override suspend fun deleteAll() {
        values.clear()
    }
}