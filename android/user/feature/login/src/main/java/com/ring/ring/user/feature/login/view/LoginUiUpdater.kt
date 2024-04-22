package com.ring.ring.user.feature.login.view

import com.ring.ring.user.feature.login.viewmodel.LoginViewModel

internal data class LoginUiUpdater(
    val setEmail: (email: String) -> Unit,
    val setPassword: (password: String) -> Unit,
    val login: () -> Unit,
)

internal fun toUpdater(viewModel: LoginViewModel) = LoginUiUpdater(
    setEmail = viewModel::setEmail,
    setPassword = viewModel::setPassword,
    login = viewModel::login,
)