package com.ring.ring.user.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

internal data class SignUpUiState(
    val email: Email,
    val password: Password,
) {
    internal data class Email(
        val input: String,
        val isError: Boolean,
        val visibleSupportingText: Boolean,
    )

    internal data class Password(
        val input: String,
        val isError: Boolean,
        val visibleSupportingText: Boolean,
    )
}

@Composable
internal fun rememberSignUpUiState(
    viewModel: SignUpViewModel,
): SignUpUiState {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    return SignUpUiState(
        email = email,
        password = password,
    )
}