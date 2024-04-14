package com.ring.ring

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ring.ring.todo.feature.create.CREATE_TODO_ROUTE
import com.ring.ring.todo.feature.create.createTodoScreen
import com.ring.ring.todo.feature.edit.EditTodoNav
import com.ring.ring.todo.feature.edit.editTodoScreen
import com.ring.ring.todo.feature.list.TODO_LIST_ROUTE
import com.ring.ring.todo.feature.list.todoListScreen
import com.ring.ring.user.feature.login.LOGIN_ROUTE
import com.ring.ring.user.feature.login.loginScreen
import com.ring.ring.user.feature.signup.SIGN_UP_ROUTE
import com.ring.ring.user.feature.signup.signUpScreen

@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController,
        startDestination = LOGIN_ROUTE,
    ) {
        loginScreen(
            toTodoListScreen = { navController.navigate(TODO_LIST_ROUTE) },
            toSignUpScreen = { navController.navigate(SIGN_UP_ROUTE) },
        )
        signUpScreen(popBackStack = navController::popBackStack)
        todoScreens(navController)
    }
}

private fun NavGraphBuilder.todoScreens(navController: NavHostController) {
    todoListScreen(
        toCreateTodoScreen = { navController.navigate(CREATE_TODO_ROUTE) },
        toEditTodoScreen = { navController.navigate(EditTodoNav.editRoute(it)) }
    )
    createTodoScreen(toTodoListScreen = { navController.navigate(TODO_LIST_ROUTE) })
    editTodoScreen(toTodoListScreen = { navController.navigate(TODO_LIST_ROUTE) })
}