package com.ring.ring.todo.feature.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewTodoListScreen() {
    WrapTodoListScreen(value)
}

@Composable
private fun WrapTodoListScreen(uiState: TodoListUiState) {
    TodoListScreen(
        uiState = uiState,
        updater = updater,
        toCreateTodoScreen = {},
//        snackBarHostState = SnackbarHostState(),
//        toSignUpScreen = {},
    )
}

private val value: TodoListUiState = TodoListUiState(
    todoList = listOf(
        TodoListUiState.Todo(
            id = 1L,
            title = "fakeTitle",
            done = false,
            deadline = "2024-01-01"
        ),
        TodoListUiState.Todo(
            id = 2L,
            title = "fakeTitle".repeat(20),
            done = true,
            deadline = "2024-12-31"
        ),
    )
)

private val updater = TodoListUiUpdater {}
