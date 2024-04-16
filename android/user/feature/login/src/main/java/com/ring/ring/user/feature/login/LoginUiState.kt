package com.ring.ring.user.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

internal data class LoginUiState(
    val email: String,
    val password: String,
)

@Composable
internal fun rememberLoginUiState(viewModel: LoginViewModel): LoginUiState {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    return uiState
}