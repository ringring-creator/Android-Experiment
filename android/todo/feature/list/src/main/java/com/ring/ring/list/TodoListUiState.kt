package com.ring.ring.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

internal data class TodoListUiState(
    val todoList: List<Todo>
) {
    internal data class Todo(
        val id: Long,
        val title: String,
        val done: Boolean,
        val deadline: String,
    )
}

@Composable
internal fun rememberTodoListUiState(
    viewModel: TodoListViewModel,
): TodoListUiState {
    val todoList by viewModel.todoList.collectAsState()
    return TodoListUiState(
        todoList = todoList,
    )
}