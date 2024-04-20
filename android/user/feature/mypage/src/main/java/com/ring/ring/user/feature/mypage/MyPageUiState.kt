package com.ring.ring.user.feature.mypage

internal data class MyPageUiState(
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