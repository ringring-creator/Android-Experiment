package com.ring.ring.todo.infra.domain

interface TodoLocalDataSource {
    suspend fun list(): List<Todo>
    suspend fun upsert(todoList: List<Todo>)
    suspend fun deleteAll()
}