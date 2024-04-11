package com.ring.ring.list

import com.ring.ring.local.LocalTodo
import com.ring.ring.local.TodoLocalDataSource
import com.ring.ring.local.UserLocalDataSource
import com.ring.ring.network.ListRequest
import com.ring.ring.network.ListResponse
import com.ring.ring.network.TodoNetworkDataSource
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

    private fun convert(todo: ListResponse.Todo): TodoListUiState.Todo {
        return TodoListUiState.Todo(
            id = todo.id,
            title = todo.title,
            done = todo.done,
            deadline = todo.deadline.toString(),
        )
    }
}