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
    WrapSignUpScreen(uiState = uiState)
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewSignUpScreenWhenEmailInvalid() {
    WrapSignUpScreen(
        uiState = uiState.copy(
            email = SignUpUiState.Email("test", true, true)
        ),
    )
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewSignUpScreenWhenPasswordInvalid() {
    WrapSignUpScreen(
        uiState = uiState.copy(
            password = SignUpUiState.Password("test", true, true)
        )
    )
}

@Composable
private fun WrapSignUpScreen(uiState: SignUpUiState) {
    SignUpScreen(
        uiState = uiState,
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