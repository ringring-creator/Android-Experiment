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
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val done by viewModel.done.collectAsState()
    val deadline by viewModel.deadline.collectAsState()
    val isShowDatePicker by viewModel.isShowDatePicker.collectAsState()
    return EditTodoUiState(
        title = title,
        description = description,
        done = done,
        deadline = deadline,
        isShowDatePicker = isShowDatePicker,
    )
}