package com.ring.ring.todo.infra.domain

interface TodoNetworkDataSource {
    suspend fun fetchList(token: String): List<Todo>
    suspend fun fetch(todoId: Long, token: String): Todo
    suspend fun create(todo: Todo, token: String)
    suspend fun update(todo: Todo, token: String)
    suspend fun updateDone(todoId: Long, done: Boolean, token: String)
    suspend fun delete(todoId: Long, token: String)
}