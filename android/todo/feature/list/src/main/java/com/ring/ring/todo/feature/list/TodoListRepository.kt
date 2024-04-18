package com.ring.ring.todo.feature.list

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.user.infra.model.UserLocalDataSource
import com.ring.ring.util.date.DateUtil
import javax.inject.Inject

internal class TodoListRepository @Inject constructor(
    private val networkDataSource: TodoNetworkDataSource,
    private val localDataSource: TodoLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val dateUtil: DateUtil,
) {
    suspend fun list(): List<TodoListUiState.Todo> {
        return try {
            val user = userLocalDataSource.getUser()!!
            val fetchedTodoList = networkDataSource.list(user.token)
            localDataSource.deleteAll()
            localDataSource.upsert(fetchedTodoList)
            fetchedTodoList.mapNotNull(this::convert)
        } catch (e: Throwable) {
            localDataSource
                .list()
                .mapNotNull(this::convert)
        }
    }

    suspend fun editDone(id: Long, done: Boolean) {
        val user = userLocalDataSource.getUser()!!
        networkDataSource.editDone(id, done, user.token)
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
