package com.ring.ring.user.feature.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ring.ring.user.feature.login.view.LoginScreen

const val LOGIN_ROUTE = "LoginRoute"

fun NavGraphBuilder.loginScreen(
    toTodoListScreen: () -> Unit,
    toSignUpScreen: () -> Unit,
) {
    composable(LOGIN_ROUTE) {
        LoginScreen(
            toTodoListScreen = toTodoListScreen,
            toSignUpScreen = toSignUpScreen,
        )
    }
}
