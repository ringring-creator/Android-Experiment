package com.ring.ring.user.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

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
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    return SignUpUiState(
        email = email,
        password = password,
    )
}