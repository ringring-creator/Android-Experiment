package com.ring.ring.todo.feature.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val TODO_LIST_ROUTE = "TodoListRoute"

fun NavGraphBuilder.todoListScreen(
    toCreateTodoScreen: () -> Unit,
    toEditTodoScreen: (id: String) -> Unit,
    toMyPageScreen: () -> Unit,
) {
    composable(TODO_LIST_ROUTE) {
        TodoListScreen(
            toCreateTodoScreen = toCreateTodoScreen,
            toEditTodoScreen = toEditTodoScreen,
            toMyPageScreen = toMyPageScreen,
        )
    }
}
