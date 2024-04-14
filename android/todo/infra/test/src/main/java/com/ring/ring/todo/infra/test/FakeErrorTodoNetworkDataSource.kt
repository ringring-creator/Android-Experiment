package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.network.TodoNetworkDataSource

class FakeErrorTodoNetworkDataSource : TodoNetworkDataSource {
    override suspend fun list(token: String): List<Todo> {
        throw Exception()
    }

    override suspend fun get(todoId: Long, token: String): Todo {
        throw Exception()
    }

    override suspend fun create(todo: Todo, token: String) {
        throw Exception()
    }

    override suspend fun edit(todo: Todo, token: String) {
        throw Exception()
    }

    override suspend fun editDone(todoId: Long, done: Boolean, token: String) {
        throw Exception()
    }

    override suspend fun delete(todoId: Long, token: String) {
        throw Exception()
    }
}
