package com.ring.ring.user.feature.mypage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

internal data class MyPageUiState(
    val email: Email,
    val password: Password,
    val expandedAction: Boolean,
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
    viewModel: MyPageViewModel,
): MyPageUiState {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    return uiState
}