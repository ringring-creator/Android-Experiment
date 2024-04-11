package com.ring.ring.test

import com.ring.ring.network.ListRequest
import com.ring.ring.network.ListResponse
import com.ring.ring.network.TodoNetworkDataSource
import kotlinx.datetime.LocalDate

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
            LocalDate(2024, 1, 1),
            1L,
        ),
        ListResponse.Todo(
            2,
            "fakeTitle2",
            "fakeDescription2",
            true,
            LocalDate(2024, 12, 31),
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
}