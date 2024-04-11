package com.ring.ring.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.local.LocalTodo
import com.ring.ring.network.ListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel() {
    private val _todoList = MutableStateFlow(emptyList<TodoListUiState.Todo>())
    val todoList = _todoList.asStateFlow()

    fun fetchTodoList() {
        viewModelScope.launch {
            _todoList.value = todoRepository.list()
        }
    }

    fun toggleDone(todoId: Long) {
        TODO("Not yet implemented")
    }
}

fun ListResponse.Todo.toLocalTodo(): LocalTodo {
    return LocalTodo(
        id = id,
        title = title,
        description = description,
        done = done,
        deadline = deadline,
    )
}