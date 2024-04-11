package com.ring.ring.test

import com.ring.ring.local.LocalTodo
import com.ring.ring.local.TodoLocalDataSource

class FakeTodoLocalDataSource : TodoLocalDataSource {
    private var todos: MutableList<LocalTodo> = mutableListOf()
    override suspend fun list(): List<LocalTodo> {
        return todos
    }

    override suspend fun upsert(todoList: List<LocalTodo>) {
        todos.addAll(todoList)
    }

    override suspend fun deleteAll() {
        todos.clear()
    }
}