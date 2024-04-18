package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource

class FakeErrorTodoNetworkDataSource : TodoNetworkDataSource {
    override suspend fun fetchList(token: String): List<Todo> {
        throw Exception()
    }

    override suspend fun fetch(todoId: Long, token: String): Todo {
        throw Exception()
    }

    override suspend fun create(todo: Todo, token: String) {
        throw Exception()
    }

    override suspend fun update(todo: Todo, token: String) {
        throw Exception()
    }

    override suspend fun updateDone(todoId: Long, done: Boolean, token: String) {
        throw Exception()
    }

    override suspend fun delete(todoId: Long, token: String) {
        throw Exception()
    }
}
