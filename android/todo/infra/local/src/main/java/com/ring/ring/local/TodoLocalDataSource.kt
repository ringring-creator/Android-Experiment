package com.ring.ring.local

interface TodoLocalDataSource {
    suspend fun list(): List<LocalTodo>
    suspend fun upsert(todoList: List<LocalTodo>)
    suspend fun deleteAll()
}