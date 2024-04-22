package com.ring.ring.user.feature.mypage

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ring.ring.user.feature.mypage.view.MyPageScreen

const val MY_PAGE_ROUTE = "MyPageRoute"

fun NavGraphBuilder.myPageScreen(
    toLoginScreen: () -> Unit,
    toTodoListScreen: () -> Unit,
    toMyPageScreen: () -> Unit,
) {
    composable(MY_PAGE_ROUTE) {
        MyPageScreen(
            toLoginScreen = toLoginScreen,
            toTodoListScreen = toTodoListScreen,
            toMyPageScreen = toMyPageScreen,
        )
    }
}