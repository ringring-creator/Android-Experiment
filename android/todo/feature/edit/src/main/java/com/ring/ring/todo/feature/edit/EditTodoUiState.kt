package com.ring.ring.todo.feature.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

internal data class EditTodoUiState(
    val todo: Todo,
    val isShowDatePicker: Boolean,
) {
    data class Todo(
        val title: String,
        val description: String,
        val done: Boolean,
        val deadline: String,
    )
}

@Composable
internal fun rememberEditTodoUiState(
    viewModel: EditTodoViewModel
): EditTodoUiState {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    return uiState
}