package com.ring.ring.todo.feature.edit

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

class EditTodoNav {
    val route = "EditTodoRoute/{$ID}"

    companion object {
        const val ID = "id"
        fun editRoute(id: String) = "EditTodoRoute/$id"
    }
}

fun NavGraphBuilder.editTodoScreen(
    toTodoListScreen: () -> Unit,
    toLoginScreen: () -> Unit,
) {
    composable(
        route = EditTodoNav().route,
        arguments = listOf(navArgument(EditTodoNav.ID) {
            type = NavType.LongType
        }),
    ) {
        com.ring.ring.todo.feature.edit.view.EditTodoScreen(
            toTodoListScreen = toTodoListScreen,
            toLoginScreen = toLoginScreen,
        )
    }
}
