package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.domain.Todo

interface TodoNetworkDataSource {
    suspend fun list(token: String): List<Todo>
    suspend fun get(todoId: Long, token: String): Todo
    suspend fun create(todo: Todo, token: String)
    suspend fun edit(todo: Todo, token: String)
    suspend fun editDone(todoId: Long, done: Boolean, token: String)
    suspend fun delete(todoId: Long, token: String)
}