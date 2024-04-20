package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.dto.CreateRequest
import com.ring.ring.todo.infra.network.dto.EditDoneRequest
import com.ring.ring.todo.infra.network.dto.EditRequest
import com.ring.ring.todo.infra.network.dto.TodoModel
import com.ring.ring.todo.infra.network.exception.UnauthorizedException
import retrofit2.HttpException
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import javax.inject.Inject

class TodoRetrofitDataSource @Inject constructor(
    private val networkApi: RetrofitTodoNetworkApi,
) : TodoNetworkDataSource {
    override suspend fun fetchList(token: String): List<Todo> {
        return try {
            networkApi
                .list("Bearer $token")
                .todoList
                .map(TodoModel::toTodo)
        } catch (e: Throwable) {
            throwUnauthorizedExceptionIfNeeded(e)
            throw e
        }
    }

    override suspend fun fetch(todoId: Long, token: String): Todo {
        return try {
            networkApi.get(
                todoId = todoId, "Bearer $token"
            )
                .todo
                .toTodo()
        } catch (e: Throwable) {
            throwUnauthorizedExceptionIfNeeded(e)
            throw e
        }
    }

    override suspend fun create(todo: Todo, token: String) {
        try {
            networkApi.create(
                CreateRequest(todo = todo.toTodoModel()),
                "Bearer $token"
            )
        } catch (e: Throwable) {
            throwUnauthorizedExceptionIfNeeded(e)
            throw e
        }
    }

    override suspend fun update(todo: Todo, token: String) {
        return try {
            val todoId = todo.id ?: return
            networkApi.edit(
                todoId = todoId,
                EditRequest(todo = todo.toTodoModel()),
                "Bearer $token",
            )
        } catch (e: Throwable) {
            throwUnauthorizedExceptionIfNeeded(e)
            throw e
        }
    }

    override suspend fun updateDone(todoId: Long, done: Boolean, token: String) {
        try {
            networkApi.editDone(
                todoId = todoId,
                EditDoneRequest(done),
                "Bearer $token"
            )
        } catch (e: Throwable) {
            throwUnauthorizedExceptionIfNeeded(e)
            throw e
        }
    }

    override suspend fun delete(todoId: Long, token: String) {
        try {
            networkApi.delete(
                todoId = todoId,
                authorization = "Bearer $token"
            )
        } catch (e: Throwable) {
            throwUnauthorizedExceptionIfNeeded(e)
            throw e
        }
    }

    private fun Todo.toTodoModel(): TodoModel = TodoModel(
        id = id,
        title = title,
        description = description,
        done = done,
        deadline = deadline,
    )

    private fun throwUnauthorizedExceptionIfNeeded(e: Throwable) {
        if (e is HttpException && e.code() == HTTP_UNAUTHORIZED) {
            throw UnauthorizedException()
        }
    }
}
