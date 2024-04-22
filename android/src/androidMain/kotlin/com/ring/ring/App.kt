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
import com.ring.ring.user.feature.mypage.My_PAGE_ROUTE
import com.ring.ring.user.feature.mypage.myPageScreen
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
        userScreens(navController)
        todoScreens(navController)
    }
}

private fun NavGraphBuilder.userScreens(navController: NavHostController) {
    loginScreen(
        toTodoListScreen = { navController.navigate(TODO_LIST_ROUTE) },
        toSignUpScreen = { navController.navigate(SIGN_UP_ROUTE) },
    )
    signUpScreen(
        popBackStack = navController::popBackStack,
    )
    myPageScreen(
        toLoginScreen = { navController.navigate(LOGIN_ROUTE) },
        toTodoListScreen = { navController.navigate(TODO_LIST_ROUTE) },
        toMyPageScreen = { navController.navigate(My_PAGE_ROUTE) },
    )
}

private fun NavGraphBuilder.todoScreens(navController: NavHostController) {
    todoListScreen(
        toTodoListScreen = { navController.navigate(TODO_LIST_ROUTE) },
        toCreateTodoScreen = { navController.navigate(CREATE_TODO_ROUTE) },
        toEditTodoScreen = { navController.navigate(EditTodoNav.editRoute(it)) },
        toMyPageScreen = { navController.navigate(My_PAGE_ROUTE) },
    )
    createTodoScreen(
        toTodoListScreen = { navController.navigate(TODO_LIST_ROUTE) },
        toLoginScreen = { navController.navigate(LOGIN_ROUTE) },
    )
    editTodoScreen(
        toTodoListScreen = { navController.navigate(TODO_LIST_ROUTE) },
        toLoginScreen = { navController.navigate(LOGIN_ROUTE) },
    )
}
