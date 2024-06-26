package com.ring.ring.todo.feature.edit.view

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ring.ring.util.date.DefaultDateUtil
import kotlinx.datetime.Clock

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewEditTodoScreen() {
    EditTodoScreen(value)
}

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewEditTodoScreenWithLongValue() {
    EditTodoScreen(
        value.copy(
            todo = value.todo.copy(
                title = "fakeTitle".repeat(20),
                description = "fakeDescription".repeat(20),
            ),
        )
    )
}

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewEditTodoScreenShowDatePicker() {
    EditTodoScreen(
        value.copy(
            isShowDatePicker = true
        )
    )
}

@Composable
private fun EditTodoScreen(uiState: EditTodoUiState) {
    EditTodoScreen(
        uiState = uiState,
        updater = updater,
        toTodoListScreen = { },
        snackbarHostState = SnackbarHostState(),
    )
}

private val dateUtil = DefaultDateUtil()

private val value: EditTodoUiState = EditTodoUiState(
    todo = EditTodoUiState.Todo(
        title = "title",
        description = "description",
        done = true,
        deadline = dateUtil.format(Clock.System.now()),
    ),
    isShowDatePicker = false,
)

private val updater = EditTodoUiUpdater({}, {}, {}, {}, {}, {}, {}, {})
