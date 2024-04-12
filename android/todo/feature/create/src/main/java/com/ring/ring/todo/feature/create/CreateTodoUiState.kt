package com.ring.ring.todo.feature.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

internal data class CreateTodoUiState(
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: Deadline,
    val isShowDatePicker: Boolean,
) {
    data class Deadline(
        val dateMillis: Long
    ) {
        fun formatString(): String {
            return DateUtil.format(dateMillis)
        }
    }
}

@Composable
internal fun rememberCreateTodoUiState(
    viewModel: CreateTodoViewModel,
): CreateTodoUiState {
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val done by viewModel.done.collectAsState()
    val deadline by viewModel.deadline.collectAsState()
    val isShowDatePicker by viewModel.isShowDatePicker.collectAsState()

    return CreateTodoUiState(
        title = title,
        description = description,
        done = done,
        deadline = deadline,
        isShowDatePicker = isShowDatePicker,
    )
}

