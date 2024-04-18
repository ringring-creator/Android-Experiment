package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource

class FakeTodoLocalDataSource(
    private var values: MutableList<Todo> = mutableListOf(),
) : TodoLocalDataSource {
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