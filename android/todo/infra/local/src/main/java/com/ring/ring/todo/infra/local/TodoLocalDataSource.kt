package com.ring.ring.todo.infra.local

import com.ring.ring.todo.infra.domain.Todo

interface TodoLocalDataSource {
    suspend fun list(): List<Todo>
    suspend fun upsert(todoList: List<Todo>)
    suspend fun deleteAll()
}