package com.ring.ring.todo.infra.local

import com.ring.ring.infra.db.TodoDao
import com.ring.ring.infra.db.TodoEntity
import com.ring.ring.todo.infra.domain.Todo
import javax.inject.Inject

class TodoDatabaseDataSource @Inject constructor(
    private val dao: TodoDao
) : TodoLocalDataSource {
    override suspend fun list(): List<Todo> {
        return dao.getAll().map { it.toTodo() }
    }

    override suspend fun upsert(todoList: List<Todo>) {
        dao.upsertList(
            todo = todoList.map { it.toTodoEntity() }
        )
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    private fun TodoEntity.toTodo(): Todo = Todo(
        id = id,
        title = title,
        description = description,
        done = done,
        deadline = deadline,
    )

    private fun Todo.toTodoEntity(): TodoEntity = TodoEntity(
        id = id,
        title = title,
        description = description,
        done = done,
        deadline = deadline,
    )
}