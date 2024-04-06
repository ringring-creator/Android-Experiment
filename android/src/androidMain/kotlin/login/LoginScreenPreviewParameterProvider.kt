package login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class LoginScreenPreviewParameterProvider : PreviewParameterProvider<LoginUiState> {
    override val values: Sequence<LoginUiState>
        get() = sequenceOf(
            value,
        )

    private val value: LoginUiState = LoginUiState(
        email = "test@gmail.com",
        password = "testtest"
    )
}
