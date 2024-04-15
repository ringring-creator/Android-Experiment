package com.ring.ring.todo.feature.edit

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.user.infra.model.UserLocalDataSource
import kotlinx.datetime.Instant
import javax.inject.Inject

internal class TodoRepository @Inject constructor(
    private val networkDataSource: TodoNetworkDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) {
    suspend fun getTodo(id: Long): Todo {
        val token = userLocalDataSource.getUser()!!.token
        return networkDataSource.get(todoId = id, token)
    }

    suspend fun editTodo(
        id: Long,
        title: String,
        description: String,
        done: Boolean,
        deadline: Instant
    ) {
        val token = userLocalDataSource.getUser()!!.token
        networkDataSource.edit(
            Todo(
                id = id,
                title = title,
                description = description,
                done = done,
                deadline = deadline,
            ),
            token
        )
    }

    suspend fun deleteTodo(id: Long) {
        val token = userLocalDataSource.getUser()!!.token
        networkDataSource.delete(todoId = id, token)
    }
}