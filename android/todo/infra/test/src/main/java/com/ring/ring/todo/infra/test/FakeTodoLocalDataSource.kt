package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.local.LocalTodo
import com.ring.ring.todo.infra.local.TodoLocalDataSource

class FakeTodoLocalDataSource(
    private val isSimulateError: Boolean = false,
) : TodoLocalDataSource {
    private var todos: MutableList<LocalTodo> = mutableListOf()
    override suspend fun list(): List<LocalTodo> {
        if (isSimulateError) throw Exception()
        return todos
    }

    override suspend fun upsert(todoList: List<LocalTodo>) {
        if (isSimulateError) throw Exception()
        todos.addAll(todoList)
    }

    override suspend fun deleteAll() {
        if (isSimulateError) throw Exception()
        todos.clear()
    }
}