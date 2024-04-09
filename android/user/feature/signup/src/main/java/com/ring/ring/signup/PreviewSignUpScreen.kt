package com.ring.ring.signup

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewSignUpScreen() {
    SignUpScreen(
        uiState = uiState,
        updater = updater,
        snackBarHostState = SnackbarHostState(),
        toLoginScreen = {},
    )
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewSignUpScreenWhenEmailInvalid() {
    SignUpScreen(
        uiState = uiState.copy(
            email = SignUpUiState.Email("test", true, true)
        ),
        updater = updater,
        snackBarHostState = SnackbarHostState(),
        toLoginScreen = {},
    )
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewSignUpScreenWhenPasswordInvalid() {
    SignUpScreen(
        uiState = uiState.copy(
            password = SignUpUiState.Password("test", true, true)
        ),
        updater = updater,
        snackBarHostState = SnackbarHostState(),
        toLoginScreen = {},
    )
}

private val uiState = SignUpUiState(
    email = SignUpUiState.Email("test@gmail.com", false, false),
    password = SignUpUiState.Password("Testtest1", false, false)
)

private val updater = SignUpUiUpdater({}, {}, {})