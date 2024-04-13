package com.ring.ring.todo.feature.edit

import com.ring.ring.todo.infra.network.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.request.DeleteRequest
import com.ring.ring.todo.infra.network.request.EditRequest
import com.ring.ring.todo.infra.network.request.GetRequest
import com.ring.ring.todo.infra.network.response.GetResponse
import com.ring.ring.user.infra.local.UserLocalDataSource
import kotlinx.datetime.Instant
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val networkDataSource: TodoNetworkDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) {
    suspend fun getTodo(id: Long): GetResponse {
        val token = userLocalDataSource.getUser()!!.token
        return networkDataSource.get(GetRequest(todoId = id), token)
    }

    suspend fun editTodo(
        id: Long,
        title: String,
        description: String,
        done: Boolean,
        deadline: Instant
    ) {
        val token = userLocalDataSource.getUser()!!.token
        networkDataSource.edit(
            EditRequest(
                id = id,
                title = title,
                description = description,
                done = done,
                deadline = deadline,
            ),
            token
        )
    }

    suspend fun deleteTodo(id: Long) {
        val token = userLocalDataSource.getUser()!!.token
        networkDataSource.delete(DeleteRequest(id), token)
    }

}