package com.ring.ring.todo.feature.list

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.local.TodoLocalDataSource
import com.ring.ring.todo.infra.network.TodoNetworkDataSource
import com.ring.ring.user.infra.local.UserLocalDataSource
import javax.inject.Inject

internal class TodoRepository @Inject constructor(
    private val networkDataSource: TodoNetworkDataSource,
    private val localDataSource: TodoLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) {
    suspend fun list(): List<TodoListUiState.Todo> {
        return try {
            val user = userLocalDataSource.getUser()!!
            val fetchedTodoList = networkDataSource.list(user.token)
            localDataSource.deleteAll()
            localDataSource.upsert(fetchedTodoList)
            fetchedTodoList.mapNotNull { convert(it) }
        } catch (e: Throwable) {
            localDataSource
                .list()
                .mapNotNull(Todo::toTodo)
        }
    }

    suspend fun editDone(id: Long, done: Boolean) {
        val user = userLocalDataSource.getUser()!!
        networkDataSource.editDone(id, done, user.token)
    }

    private fun convert(todo: Todo): TodoListUiState.Todo? {
        return todo.id?.let {
            TodoListUiState.Todo(
                id = it,
                title = todo.title,
                done = todo.done,
                deadline = todo.deadline.toString(),
            )
        }
    }
}