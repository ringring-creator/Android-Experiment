package com.ring.ring.user.feature.signup

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ring.ring.user.feature.signup.view.SignUpScreen

const val SIGN_UP_ROUTE = "SignUpRoute"

fun NavGraphBuilder.signUpScreen(
    popBackStack: () -> Unit,
) {
    composable(SIGN_UP_ROUTE) {
        SignUpScreen(
            toLoginScreen = popBackStack,
        )
    }
}
