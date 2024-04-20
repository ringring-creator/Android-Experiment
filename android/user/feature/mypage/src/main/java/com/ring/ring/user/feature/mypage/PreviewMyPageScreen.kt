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
    MyPageScreen(uiState = uiState)
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreenWhenEmailInvalid() {
    MyPageScreen(
        uiState = uiState.copy(
            email = MyPageUiState.Email("test", true, false)
        ),
    )
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreenWhenShowSupportingText() {
    MyPageScreen(
        uiState = uiState.copy(
            email = MyPageUiState.Email("test", true, true)
        ),
    )
}

@Preview(
    group = "error scenario",
    showSystemUi = true,
    apiLevel = 34,
)
@Composable
internal fun PreviewMyPageScreenWhenPasswordInvalid() {
    MyPageScreen(
        uiState = uiState.copy(
            password = MyPageUiState.Password("test", true)
        )
    )
}


@Composable
private fun MyPageScreen(uiState: MyPageUiState) {
    MyPageScreen(
        uiState = uiState,
        updater = updater,
    )
}

private val uiState: MyPageUiState = MyPageUiState(
    email = MyPageUiState.Email("test@gmail.com", false, false),
    password = MyPageUiState.Password("Testtest1", false)
)

private val updater: MyPageUiUpdater = MyPageUiUpdater({}, {}, {}, {})