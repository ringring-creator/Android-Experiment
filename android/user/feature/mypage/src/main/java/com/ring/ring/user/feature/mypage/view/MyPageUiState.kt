package com.ring.ring.user.feature.mypage.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ring.ring.user.feature.mypage.viewmodel.MyPageViewModel

internal data class MyPageUiState(
    val email: Email,
    val password: Password,
    val expandedAction: Boolean,
    val showDialog: Boolean,
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