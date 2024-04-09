package com.ring.ring.login

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
        updater = updater
    ) {}
}

private val value: LoginUiState = LoginUiState(
    email = "test@gmail.com",
    password = "testtest"
)

private val updater = LoginUiUpdater({}, {}, {})
