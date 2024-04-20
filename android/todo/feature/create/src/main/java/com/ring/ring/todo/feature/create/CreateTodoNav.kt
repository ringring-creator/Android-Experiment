package com.ring.ring.todo.feature.create

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val CREATE_TODO_ROUTE = "CreateTodoRoute"

fun NavGraphBuilder.createTodoScreen(
    toTodoListScreen: () -> Unit,
    toLoginScreen: () -> Unit,
) {
    composable(CREATE_TODO_ROUTE) {
        CreateTodoScreen(
            toTodoListScreen = toTodoListScreen,
            toLoginScreen = toLoginScreen,
        )
    }
}
