package com.ring.ring.todo.feature.list

import com.ring.ring.todo.infra.local.LocalTodo
import com.ring.ring.todo.infra.local.TodoLocalDataSource
import com.ring.ring.todo.infra.network.EditDoneRequest
import com.ring.ring.todo.infra.network.ListRequest
import com.ring.ring.todo.infra.network.ListResponse
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
            val fetchedTodoList = networkDataSource
                .list(ListRequest(user.userId), user.token)
                .todoList
            localDataSource.upsert(fetchedTodoList.map(ListResponse.Todo::toLocalTodo))
            fetchedTodoList.map { convert(it) }
        } catch (e: Throwable) {
            localDataSource
                .list()
                .mapNotNull(LocalTodo::toTodo)
        }
    }

    suspend fun editDone(id: Long, done: Boolean) {
        val user = userLocalDataSource.getUser()!!
        networkDataSource.editDone(EditDoneRequest(id, done), user.token)
    }

    private fun convert(todo: ListResponse.Todo): TodoListUiState.Todo {
        return TodoListUiState.Todo(
            id = todo.id,
            title = todo.title,
            done = todo.done,
            deadline = todo.deadline.toString(),
        )
    }
}