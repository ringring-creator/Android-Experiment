package com.ring.ring.todo.feature.list.view

import com.airbnb.mvrx.MavericksState

internal data class TodoListUiState(
    val todoList: List<Todo>
) : MavericksState {
    internal data class Todo(
        val id: Long,
        val title: String,
        val done: Boolean,
        val deadline: String,
    )
}

//@Composable
//internal fun rememberTodoListUiState(
//    viewModel: TodoListViewModel,
//): TodoListUiState {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//    return uiState
//}