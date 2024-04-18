package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource

class FakeTodoNetworkDataSource(
    private val token: String = "",
    private var values: MutableList<Todo> = mutableListOf(),
) : TodoNetworkDataSource {
    override suspend fun list(token: String): List<Todo> {
        if (token != this.token) throw Exception()
        return values
    }

    override suspend fun get(todoId: Long, token: String): Todo {
        if (token != this.token) throw Exception()
        return values.find { it.id == todoId } ?: throw Exception()
    }

    override suspend fun create(todo: Todo, token: String) {
        if (token != this.token) throw Exception()
        val id = if (values.isEmpty()) 0 else values.maxOf { it.id ?: 0 }
        values.add(element = todo.copy(id = id + 1L))
    }

    override suspend fun edit(todo: Todo, token: String) {
        if (token != this.token) throw Exception()
        values = values.map {
            if (it.id == todo.id) todo else it
        }.toMutableList()
    }

    override suspend fun editDone(todoId: Long, done: Boolean, token: String) {
        if (token != this.token) throw Exception()
        values = values.map {
            if (it.id == todoId) it.copy(done = done) else it
        }.toMutableList()
    }

    override suspend fun delete(todoId: Long, token: String) {
        if (token != this.token) throw Exception()
        values.removeIf { it.id == todoId }
    }
}
