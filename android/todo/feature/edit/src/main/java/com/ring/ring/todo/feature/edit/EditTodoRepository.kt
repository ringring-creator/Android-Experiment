package com.ring.ring.todo.feature.edit

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.util.date.DateUtil
import kotlinx.datetime.Instant
import javax.inject.Inject

internal class EditTodoRepository @Inject constructor(
    private val networkDataSource: TodoNetworkDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val dateUtil: DateUtil,
) {
    data class FetchTodoResponse(
        val todo: EditTodoUiState.Todo,
        val deadline: Instant,
    )

    suspend fun fetchTodo(id: Long): FetchTodoResponse {
        val token = userLocalDataSource.getUser()!!.token
        val todo = networkDataSource.fetch(todoId = id, token)
        return FetchTodoResponse(
            todo = convert(todo),
            deadline = todo.deadline,
        )
    }

    suspend fun updateTodo(
        id: Long,
        todo: EditTodoUiState.Todo,
        deadline: Instant
    ) {
        val token = userLocalDataSource.getUser()!!.token
        networkDataSource.update(
            todo = Todo(
                id = id,
                title = todo.title,
                description = todo.description,
                done = todo.done,
                deadline = deadline
            ),
            token = token,
        )
    }

    suspend fun deleteTodo(id: Long) {
        val token = userLocalDataSource.getUser()!!.token
        networkDataSource.delete(todoId = id, token)
    }

    private fun convert(todo: Todo): EditTodoUiState.Todo {
        return EditTodoUiState.Todo(
            title = todo.title,
            description = todo.description,
            done = todo.done,
            deadline = dateUtil.format(todo.deadline),
        )
    }
}