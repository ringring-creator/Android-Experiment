package com.ring.ring.todo.feature.list.viewmodel

import com.ring.ring.todo.feature.list.view.TodoListUiState
import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.network.exception.UnauthorizedException
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.util.date.DateUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class TodoListRepository @Inject constructor(
    private val networkDataSource: TodoNetworkDataSource,
    private val localDataSource: TodoLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val dateUtil: DateUtil,
) {
    fun getTodoListStream(): Flow<List<TodoListUiState.Todo>> {
        return localDataSource
            .getTodoListStream()
            .map { todoList -> todoList.mapNotNull(this::convert) }
    }

    suspend fun refresh() {
        val token = userLocalDataSource.getUser()?.token ?: throw UnauthorizedException()
        val fetchedTodoList = networkDataSource.fetchList(token)
        localDataSource.deleteAll()
        localDataSource.upsert(fetchedTodoList)
    }

    suspend fun editDone(id: Long, done: Boolean) {
        val token = userLocalDataSource.getUser()?.token ?: throw UnauthorizedException()
        networkDataSource.updateDone(id, done, token)
        localDataSource.updateDone(id, done)
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
