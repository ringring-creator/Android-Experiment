package com.ring.ring.todo.infra.local

import com.ring.ring.infra.db.TodoDao
import com.ring.ring.infra.db.TodoEntity
import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoDatabaseDataSource @Inject constructor(
    private val dao: TodoDao
) : TodoLocalDataSource {
    override fun getTodoListStream(): Flow<List<Todo>> {
        return dao.getTodoListStream()
            .map { todoEntities ->
                todoEntities.map { it.toTodo() }
            }
    }

    override suspend fun upsert(todoList: List<Todo>) {
        dao.upsertList(
            todo = todoList.map { it.toTodoEntity() }
        )
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun updateDone(id: Long, done: Boolean) {
        dao.updateDone(id, done)
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