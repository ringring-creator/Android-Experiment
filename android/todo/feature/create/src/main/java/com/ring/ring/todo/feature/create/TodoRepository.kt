package com.ring.ring.todo.feature.create

import com.ring.ring.todo.infra.network.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.request.CreateRequest
import com.ring.ring.user.infra.local.UserLocalDataSource
import kotlinx.datetime.Instant
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val networkDataSource: TodoNetworkDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) {
    suspend fun create(
        title: String,
        description: String,
        done: Boolean,
        deadline: Instant,
    ) {
        networkDataSource.create(
            CreateRequest(
                title = title,
                description = description,
                done = done,
                deadline = deadline,
            ),
            userLocalDataSource.getUser()!!.token
        )
    }
}