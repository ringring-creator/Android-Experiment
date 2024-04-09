package com.ring.ring

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ring.ring.login.LOGIN_ROUTE
import com.ring.ring.login.loginScreen
import com.ring.ring.signup.SIGN_UP_ROUTE
import com.ring.ring.signup.signUpScreen

@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController,
        startDestination = LOGIN_ROUTE,
    ) {
        loginScreen(
            toTodoListScreen = {},
            toSignUpScreen = { navController.navigate(SIGN_UP_ROUTE) },
        )
        signUpScreen(popBackStack = navController::popBackStack)
    }
}