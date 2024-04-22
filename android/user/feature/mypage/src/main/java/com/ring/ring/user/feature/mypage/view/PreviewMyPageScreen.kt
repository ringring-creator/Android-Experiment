package com.ring.ring.user.feature.mypage.view

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreen() {
    MyPageScreen(uiState = uiState)
}

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreenWhenDrawerOpen() {
    MyPageScreen(
        uiState = uiState,
        drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    )
}

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreenWhenShowDialog() {
    MyPageScreen(
        uiState = uiState.copy(showDialog = true),
    )
}

//Does not work
@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreenWhenShowAction() {
    MyPageScreen(
        uiState = uiState.copy(expandedAction = true),
    )
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreenWhenEmailInvalid() {
    MyPageScreen(
        uiState = uiState.copy(
            email = MyPageUiState.Email("test", true, false)
        ),
    )
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreenWhenShowSupportingText() {
    MyPageScreen(
        uiState = uiState.copy(
            email = MyPageUiState.Email("test", true, true)
        ),
    )
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreenWhenPasswordInvalid() {
    MyPageScreen(
        uiState = uiState.copy(
            password = MyPageUiState.Password("test", true)
        )
    )
}


@Composable
private fun MyPageScreen(
    uiState: MyPageUiState,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {
    MyPageScreen(
        uiState = uiState,
        updater = updater,
        toTodoListScreen = {},
        toMyPageScreen = {},
        snackbarHostState = SnackbarHostState(),
        drawerState = drawerState,
    )
}

private val uiState: MyPageUiState = MyPageUiState(
    email = MyPageUiState.Email("test@gmail.com", false, false),
    password = MyPageUiState.Password("Testtest1", false),
    expandedAction = false,
    showDialog = false,
)

private val updater: MyPageUiUpdater = MyPageUiUpdater({}, {}, {}, {}, {}, {}, {})