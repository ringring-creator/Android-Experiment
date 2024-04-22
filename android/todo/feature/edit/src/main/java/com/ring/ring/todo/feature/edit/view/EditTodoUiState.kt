package com.ring.ring.todo.feature.edit.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ring.ring.todo.feature.edit.viewmodel.EditTodoViewModel

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