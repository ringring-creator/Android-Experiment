package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.network.TodoNetworkDataSource
import kotlinx.datetime.Instant

class FakeTodoNetworkDataSource(
    private val parameter: Parameter = Parameter(0L, "")
) : TodoNetworkDataSource {
    data class Parameter(
        val userId: Long,
        val token: String,
    )

    private var values = mutableListOf(
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


    override suspend fun list(token: String): List<Todo> {
        if (token != parameter.token) throw Exception()
        return values
    }

    override suspend fun get(todoId: Long, token: String): Todo {
        if (token != parameter.token) throw Exception()
        return values.find { it.id == todoId } ?: throw Exception()
    }

    override suspend fun create(todo: Todo, token: String) {
        if (token != parameter.token) throw Exception()
        val id = values.maxOf { it.id ?: 0 }
        values.add(element = todo.copy(id = id + 1L))
    }

    override suspend fun edit(todo: Todo, token: String) {
        if (token != parameter.token) throw Exception()
        values = values.map {
            if (it.id == todo.id) todo else it
        }.toMutableList()
    }

    override suspend fun editDone(todoId: Long, done: Boolean, token: String) {
        if (token != parameter.token) throw Exception()
        values = values.map {
            if (it.id == todoId) it.copy(done = done) else it
        }.toMutableList()
    }

    override suspend fun delete(todoId: Long, token: String) {
        if (token != parameter.token) throw Exception()
        values.removeIf { it.id == todoId }
    }
}
