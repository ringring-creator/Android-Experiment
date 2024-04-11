package com.ring.ring.user.feature.login

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewLoginScreen() {
    LoginScreen(
        uiState = value,
        updater = updater,
        snackBarHostState = SnackbarHostState(),
        toSignUpScreen = {},
    )
}

private val value: LoginUiState = LoginUiState(
    email = "test@gmail.com",
    password = "testtest"
)

private val updater = LoginUiUpdater({}, {}, {})
