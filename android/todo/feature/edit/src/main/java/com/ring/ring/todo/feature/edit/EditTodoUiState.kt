package com.ring.ring.todo.feature.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

internal data class EditTodoUiState(
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: String,
    val isShowDatePicker: Boolean,
)

@Composable
internal fun rememberEditTodoUiState(
    viewModel: EditTodoViewModel
): EditTodoUiState {
    val uiState by viewModel.uiState.collectAsState()
    return uiState
}