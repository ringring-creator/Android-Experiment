package com.ring.ring.todo.feature.list.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ring.ring.todo.feature.list.viewmodel.TodoListViewModel

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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    return uiState
}