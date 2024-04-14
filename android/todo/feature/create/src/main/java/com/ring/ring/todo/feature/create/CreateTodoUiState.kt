package com.ring.ring.todo.feature.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
    val title by viewModel.title.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val done by viewModel.done.collectAsStateWithLifecycle()
    val deadline by viewModel.deadline.collectAsStateWithLifecycle()
    val isShowDatePicker by viewModel.isShowDatePicker.collectAsStateWithLifecycle()

    return CreateTodoUiState(
        title = title,
        description = description,
        done = done,
        deadline = deadline,
        isShowDatePicker = isShowDatePicker,
    )
}

