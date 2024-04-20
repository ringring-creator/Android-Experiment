package com.ring.ring.todo.feature.create

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.exception.UnauthorizedException
import com.ring.ring.user.infra.model.UserLocalDataSource
import kotlinx.datetime.Instant
import javax.inject.Inject

internal class CreateTodoRepository @Inject constructor(
    private val networkDataSource: TodoNetworkDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) {
    suspend fun create(
        uiState: CreateTodoUiState,
        deadline: Instant,
    ) {
        networkDataSource.create(
            Todo(
                id = null,
                title = uiState.title,
                description = uiState.description,
                done = uiState.done,
                deadline = deadline,
            ),
            userLocalDataSource.getUser()?.token ?: throw UnauthorizedException()
        )
    }
}