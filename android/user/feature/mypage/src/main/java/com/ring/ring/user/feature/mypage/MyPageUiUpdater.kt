package com.ring.ring.user.feature.mypage

data class MyPageUiUpdater(
    val setEmail: (email: String) -> Unit,
    val setPassword: (password: String) -> Unit,
    val edit: () -> Unit,
    val withdrawal: () -> Unit,
)

internal fun toUpdater(viewModel: MyPageViewModel) = MyPageUiUpdater(
    setEmail = viewModel::setEmail,
    setPassword = viewModel::setPassword,
    edit = viewModel::edit,
    withdrawal = viewModel::withdrawal,
)