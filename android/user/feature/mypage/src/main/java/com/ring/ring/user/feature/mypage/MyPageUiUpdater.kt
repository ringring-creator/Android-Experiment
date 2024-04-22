package com.ring.ring.user.feature.mypage

data class MyPageUiUpdater(
    val setEmail: (email: String) -> Unit,
    val setPassword: (password: String) -> Unit,
    val setExpandedAction: (expandedAction: Boolean) -> Unit,
    val setShowDialog: (showDialog: Boolean) -> Unit,
    val logout: () -> Unit,
    val edit: () -> Unit,
    val withdrawal: () -> Unit,
)

internal fun toUpdater(viewModel: MyPageViewModel) = MyPageUiUpdater(
    setEmail = viewModel::setEmail,
    setPassword = viewModel::setPassword,
    setExpandedAction = viewModel::setExpandedAction,
    setShowDialog = viewModel::setShowDialog,
    logout = viewModel::logout,
    edit = viewModel::edit,
    withdrawal = viewModel::withdrawal,
)