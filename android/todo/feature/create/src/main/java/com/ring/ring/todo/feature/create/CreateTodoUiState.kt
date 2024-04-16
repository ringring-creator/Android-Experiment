package com.ring.ring.todo.feature.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

internal data class CreateTodoUiState(
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: String,
    val isShowDatePicker: Boolean,
)

@Composable
internal fun rememberCreateTodoUiState(
    viewModel: CreateTodoViewModel,
): CreateTodoUiState {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    return uiState
}

