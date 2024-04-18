package com.ring.ring.todo.infra.domain

interface TodoLocalDataSource {
    suspend fun load(): List<Todo>
    suspend fun upsert(todoList: List<Todo>)
    suspend fun deleteAll()
}