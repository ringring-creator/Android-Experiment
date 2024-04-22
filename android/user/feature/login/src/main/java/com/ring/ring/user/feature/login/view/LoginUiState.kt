package com.ring.ring.user.feature.login.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ring.ring.user.feature.login.viewmodel.LoginViewModel

internal data class LoginUiState(
    val email: String,
    val password: String,
)

@Composable
internal fun rememberLoginUiState(viewModel: LoginViewModel): LoginUiState {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    return uiState
}