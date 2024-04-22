package com.ring.ring.todo.feature.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ring.ring.todo.feature.list.view.TodoListScreen

const val TODO_LIST_ROUTE = "TodoListRoute"

fun NavGraphBuilder.todoListScreen(
    toTodoListScreen: () -> Unit,
    toCreateTodoScreen: () -> Unit,
    toEditTodoScreen: (id: String) -> Unit,
    toMyPageScreen: () -> Unit,
) {
    composable(TODO_LIST_ROUTE) {
        TodoListScreen(
            toTodoListScreen = toTodoListScreen,
            toCreateTodoScreen = toCreateTodoScreen,
            toEditTodoScreen = toEditTodoScreen,
            toMyPageScreen = toMyPageScreen,
        )
    }
}
