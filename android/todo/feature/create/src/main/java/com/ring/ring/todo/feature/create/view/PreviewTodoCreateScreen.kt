package com.ring.ring.todo.feature.create.view

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ring.ring.util.date.DefaultDateUtil

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

private val dateUtil = DefaultDateUtil()

private val value: CreateTodoUiState = CreateTodoUiState(
    title = "title",
    description = "description",
    done = true,
    deadline = dateUtil.format(dateUtil.currentInstant()),
    isShowDatePicker = false,
)

private val updater = CreateTodoUiUpdater({}, {}, {}, {}, {}, {}, {})
