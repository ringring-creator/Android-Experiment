package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.network.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.request.CreateRequest
import com.ring.ring.todo.infra.network.request.DeleteRequest
import com.ring.ring.todo.infra.network.request.EditDoneRequest
import com.ring.ring.todo.infra.network.request.EditRequest
import com.ring.ring.todo.infra.network.request.GetRequest
import com.ring.ring.todo.infra.network.response.GetResponse
import com.ring.ring.todo.infra.network.response.ListResponse
import kotlinx.datetime.Instant

class FakeTodoNetworkDataSource(
    private val isSimulateError: Boolean = false,
) : TodoNetworkDataSource {
    data class Parameter(
        val userId: Long,
        val token: String,
    )

    var parameter: Parameter = Parameter(0L, "")

    var listResponse = mutableListOf(
        ListResponse.Todo(
            1,
            "fakeTitle",
            "fakeDescription",
            false,
            Instant.parse("2024-01-01T00:00:00Z"),
            1L,
        ),
        ListResponse.Todo(
            2,
            "fakeTitle2",
            "fakeDescription2",
            true,
            Instant.parse("2024-12-31T00:00:00Z"),
            1L,
        ),
    )

    override suspend fun list(token: String): ListResponse {
        if (isSimulateError) throw Exception()
        if (token != parameter.token) throw Exception()

        return ListResponse(todoList = listResponse)
    }

    override suspend fun get(request: GetRequest, token: String): GetResponse {
        if (isSimulateError) throw Exception()
        if (token != parameter.token) throw Exception()
        return listResponse.find { it.id == request.todoId }?.toGetResponse() ?: throw Exception()
    }

    override suspend fun create(request: CreateRequest, token: String) {
        if (isSimulateError) throw Exception()
        if (token != parameter.token) throw Exception()

        listResponse.add(element = request.toListResponseTodo())
    }

    private fun CreateRequest.toListResponseTodo() = ListResponse.Todo(
        id = listResponse.maxBy { it.id }.id,
        title = title,
        description = description,
        done = done,
        deadline = deadline,
        userId = parameter.userId,
    )

    override suspend fun edit(request: EditRequest, token: String) {
        if (isSimulateError) throw Exception()
        if (token != parameter.token) throw Exception()
        listResponse = listResponse.map {
            if (it.id == request.id) editTodo(it, request) else it
        }.toMutableList()
    }

    override suspend fun editDone(request: EditDoneRequest, token: String) {
        if (isSimulateError) throw Exception()
        if (token != parameter.token) throw Exception()

        listResponse = listResponse.map {
            if (it.id == request.todoId) it.copy(done = request.done) else it
        }.toMutableList()
    }

    override suspend fun delete(request: DeleteRequest, token: String) {
        if (isSimulateError) throw Exception()
        if (token != parameter.token) throw Exception()

        listResponse.removeIf { it.id == request.todoId }
    }

    private fun ListResponse.Todo.toGetResponse(): GetResponse {
        return GetResponse(
            id = id,
            title = title,
            description = description,
            done = done,
            deadline = deadline,
        )
    }

    private fun editTodo(
        it: ListResponse.Todo,
        request: EditRequest
    ) = it.copy(
        title = request.title,
        description = request.description,
        done = request.done,
        deadline = request.deadline,
    )
}
