package com.ring.ring.todo.feature.edit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ring.ring.util.date.DateUtil
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
        snackBarHostState = SnackbarHostState(),
    )
}

private val dateUtil = DateUtil()

private val value: EditTodoUiState = EditTodoUiState(
    title = "title",
    description = "description",
    done = true,
    deadline = dateUtil.format(Clock.System.now()),
    isShowDatePicker = false,
)

private val updater = EditTodoUiUpdater({}, {}, {}, {}, {}, {}, {}, {})
