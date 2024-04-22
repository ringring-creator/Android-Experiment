package com.ring.ring.todo.feature.list.view

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewTodoListScreen() {
    TodoListScreen(uiState = value)
}

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewTodoListScreenShowNavigationDrawer() {
    TodoListScreen(
        uiState = value,
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )
}

@Composable
private fun TodoListScreen(
    uiState: TodoListUiState,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Open),
) {
    TodoListScreen(
        uiState = uiState,
        updater = updater,
        toTodoListScreen = {},
        toCreateTodoScreen = {},
        toEditTodoScreen = {},
        toMyPageScreen = {},
        snackBarHostState = SnackbarHostState(),
        drawerState = drawerState,
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
