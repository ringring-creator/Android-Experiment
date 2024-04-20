package com.ring.ring.todo.feature.list

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.exception.UnauthorizedException
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.util.date.DateUtil
import javax.inject.Inject

internal class TodoListRepository @Inject constructor(
    private val networkDataSource: TodoNetworkDataSource,
    private val localDataSource: TodoLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val dateUtil: DateUtil,
) {
    private var fetchedTodoList: List<Todo>? = null

    suspend fun list(): List<TodoListUiState.Todo> {
        return try {
            val token = userLocalDataSource.getUser()?.token ?: throw UnauthorizedException()
            fetchedTodoList = networkDataSource.fetchList(token)
            fetchedTodoList?.mapNotNull(this::convert) ?: emptyList()
        } catch (e: Throwable) {
            localDataSource
                .load()
                .mapNotNull(this::convert)
        }
    }

    suspend fun refresh() {
        fetchedTodoList?.let {
            localDataSource.deleteAll()
            localDataSource.upsert(it)
        }
    }

    suspend fun editDone(id: Long, done: Boolean) {
        val token = userLocalDataSource.getUser()?.token ?: throw UnauthorizedException()
        networkDataSource.updateDone(id, done, token)
    }

    private fun convert(todo: Todo): TodoListUiState.Todo? {
        return todo.id?.let {
            TodoListUiState.Todo(
                id = it,
                title = todo.title,
                done = todo.done,
                deadline = dateUtil.format(todo.deadline),
            )
        }
    }
}
