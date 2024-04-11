package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.network.EditDoneRequest
import com.ring.ring.todo.infra.network.ListRequest
import com.ring.ring.todo.infra.network.ListResponse
import com.ring.ring.todo.infra.network.TodoNetworkDataSource
import kotlinx.datetime.Instant

class FakeTodoNetworkDataSource(
    private val isSimulateError: Boolean = false,
) : TodoNetworkDataSource {
    data class Parameter(
        val userId: Long,
        val token: String,
    )

    var parameter: Parameter = Parameter(0L, "")

    val listResponse = listOf(
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

    override suspend fun list(request: ListRequest, token: String): ListResponse {
        if (isSimulateError) throw Exception()
        if (request.userId != parameter.userId || token != parameter.token) throw Exception()

        return ListResponse(
            todoList = listResponse
        )
    }

    var editDoneWasCalled: EditDoneParameter? = null
    override suspend fun editDone(request: EditDoneRequest, token: String) {
        editDoneWasCalled = EditDoneParameter(request, token)
    }

    data class EditDoneParameter(val request: EditDoneRequest, val token: String)
}
