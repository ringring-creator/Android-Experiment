package com.ring.ring.user.feature.signup

data class SignUpUiUpdater(
    val setEmail: (email: String) -> Unit,
    val setPassword: (password: String) -> Unit,
    val signUp: () -> Unit,
)

internal fun toUpdater(viewModel: SignUpViewModel) = SignUpUiUpdater(
    setEmail = viewModel::setEmail,
    setPassword = viewModel::setPassword,
    signUp = viewModel::signUp,
)