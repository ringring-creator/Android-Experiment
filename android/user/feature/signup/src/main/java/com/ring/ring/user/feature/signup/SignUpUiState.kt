package com.ring.ring.user.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

internal data class SignUpUiState(
    val email: Email,
    val password: Password,
) {
    internal data class Email(
        val value: String,
        val isError: Boolean,
        val isShowSupportingText: Boolean,
    )

    internal data class Password(
        val value: String,
        val isError: Boolean,
    )
}

@Composable
internal fun rememberSignUpUiState(
    viewModel: SignUpViewModel,
): SignUpUiState {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    return uiState
}