package com.ring.ring.todo.infra.local

interface TodoLocalDataSource {
    suspend fun list(): List<LocalTodo>
    suspend fun upsert(todoList: List<LocalTodo>)
    suspend fun deleteAll()
}