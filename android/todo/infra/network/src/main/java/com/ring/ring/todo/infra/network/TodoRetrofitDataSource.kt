package com.ring.ring.todo.infra.network

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.dto.CreateRequest
import com.ring.ring.todo.infra.network.dto.DeleteRequest
import com.ring.ring.todo.infra.network.dto.EditDoneRequest
import com.ring.ring.todo.infra.network.dto.EditRequest
import com.ring.ring.todo.infra.network.dto.GetRequest
import com.ring.ring.todo.infra.network.dto.TodoModel
import javax.inject.Inject

class TodoRetrofitDataSource @Inject constructor(
    private val networkApi: RetrofitTodoNetworkApi,
) : TodoNetworkDataSource {
    override suspend fun fetchList(token: String): List<Todo> {
        return networkApi
            .list("Bearer $token")
            .todoList
            .map(TodoModel::toTodo)
    }

    override suspend fun fetch(todoId: Long, token: String): Todo {
        return networkApi.get(
            GetRequest(todoId = todoId), "Bearer $token"
        )
            .todo
            .toTodo()
    }

    override suspend fun create(todo: Todo, token: String) {
        networkApi.create(
            CreateRequest(todo = todo.toTodoModel()),
            "Bearer $token"
        )
    }

    override suspend fun update(todo: Todo, token: String) {
        networkApi.edit(
            EditRequest(todo = todo.toTodoModel()),
            "Bearer $token",
        )
    }

    override suspend fun updateDone(todoId: Long, done: Boolean, token: String) {
        networkApi.editDone(
            EditDoneRequest(todoId, done),
            "Bearer $token"
        )
    }

    override suspend fun delete(todoId: Long, token: String) {
        networkApi.delete(
            DeleteRequest(todoId = todoId),
            "Bearer $token"
        )
    }

    private fun Todo.toTodoModel(): TodoModel = TodoModel(
        id = id,
        title = title,
        description = description,
        done = done,
        deadline = deadline,
    )
}