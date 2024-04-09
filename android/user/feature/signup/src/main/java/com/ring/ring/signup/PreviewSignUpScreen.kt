package com.ring.ring.signup

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
        updater = updater
    ) {}
}

private val uiState = SignUpUiState(
    email = "test@gmail.com",
    password = "testtest"
)

private val updater = SignUpUiUpdater({}, {}, {})