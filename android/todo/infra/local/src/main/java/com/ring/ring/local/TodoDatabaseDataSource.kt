package com.ring.ring.local

import com.ring.ring.db.TodoDao
import com.ring.ring.db.TodoEntity

class TodoDatabaseDataSource(
    private val dao: TodoDao
) : TodoLocalDataSource {
    override suspend fun list(): List<LocalTodo> {
        return dao.getAll().map(TodoEntity::toLocalTodo)
    }

    override suspend fun upsert(todoList: List<LocalTodo>) {
        dao.upsertList(
            todo = todoList.map(LocalTodo::toTodoEntity)
        )
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}