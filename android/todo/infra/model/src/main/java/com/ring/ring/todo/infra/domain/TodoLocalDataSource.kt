package com.ring.ring.todo.infra.domain

import kotlinx.coroutines.flow.Flow

interface TodoLocalDataSource {
    fun getTodoListStream(): Flow<List<Todo>>
    suspend fun upsert(todoList: List<Todo>)
    suspend fun deleteAll()
    suspend fun updateDone(id: Long, done: Boolean)
}