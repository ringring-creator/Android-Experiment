package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource

class FakeErrorTodoLocalDataSource : TodoLocalDataSource {
    override suspend fun list(): List<Todo> {
        throw Exception()
    }

    override suspend fun upsert(todoList: List<Todo>) {
        throw Exception()
    }

    override suspend fun deleteAll() {
        throw Exception()
    }
}