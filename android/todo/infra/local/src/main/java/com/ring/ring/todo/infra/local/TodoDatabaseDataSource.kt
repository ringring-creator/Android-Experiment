package com.ring.ring.todo.infra.local

import com.ring.ring.infra.db.TodoDao
import com.ring.ring.infra.db.TodoEntity
import javax.inject.Inject

class TodoDatabaseDataSource @Inject constructor(
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