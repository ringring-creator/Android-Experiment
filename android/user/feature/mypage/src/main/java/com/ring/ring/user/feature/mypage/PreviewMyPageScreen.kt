package com.ring.ring.user.feature.mypage

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    group = "normal scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreen() {
    MyPageScreen(
        uiState = uiState,
    )
}

private val uiState: MyPageUiState = MyPageUiState(
    email = MyPageUiState.Email("test@gmail.com", false, false),
    password = MyPageUiState.Password("Testtest1", false)
)
