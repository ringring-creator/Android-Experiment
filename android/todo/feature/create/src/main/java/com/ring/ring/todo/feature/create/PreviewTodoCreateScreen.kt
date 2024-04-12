package com.ring.ring.todo.feature.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.datetime.Clock

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewTodoCreateScreen() {
    CreateTodoScreen(value)
}

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewTodoCreateScreenWithLongText() {
    CreateTodoScreen(
        value.copy(
            title = "fakeTitle".repeat(20),
            description = "fakeDescription".repeat(20)
        )
    )
}

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewTodoCreateScreenShowDatePicker() {
    CreateTodoScreen(value.copy(isShowDatePicker = true))
}

@Composable
private fun CreateTodoScreen(uiState: CreateTodoUiState) {
    CreateTodoScreen(uiState, updater, {}, SnackbarHostState())
}

private val value: CreateTodoUiState = CreateTodoUiState(
    title = "title",
    description = "description",
    done = true,
    deadline = CreateTodoUiState.Deadline(Clock.System.now().toEpochMilliseconds()),
    isShowDatePicker = false,
)

private val updater = CreateTodoUiUpdater({}, {}, {}, {}, {}, {}, {})
