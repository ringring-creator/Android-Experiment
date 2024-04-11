package com.ring.ring.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.local.UserLocalDataSource
import com.ring.ring.network.ListRequest
import com.ring.ring.network.ListResponse
import com.ring.ring.network.TodoNetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class TodoListViewModel(
    private val networkDataSource: TodoNetworkDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : ViewModel() {
    private val _todoList = MutableStateFlow(emptyList<TodoListUiState.Todo>())
    val todoList = _todoList.asStateFlow()

    fun fetchTodoList() {
        viewModelScope.launch {
            val user = userLocalDataSource.getUser()!!
            _todoList.value =
                networkDataSource.list(ListRequest(user.userId), user.token).todoList.map {
                    convert(it)
                }
        }
    }

    private fun convert(todo: ListResponse.Todo): TodoListUiState.Todo {
        return TodoListUiState.Todo(
            id = todo.id,
            title = todo.title,
            done = todo.done,
            deadline = todo.deadline.toString(),
        )
    }

    fun toggleDone(todoId: Long) {
        TODO("Not yet implemented")
    }
}