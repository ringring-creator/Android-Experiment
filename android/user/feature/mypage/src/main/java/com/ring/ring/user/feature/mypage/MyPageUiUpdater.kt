package com.ring.ring.user.feature.mypage

data class MyPageUiUpdater(
    val setEmail: (email: String) -> Unit,
    val setPassword: (password: String) -> Unit,
    val edit: () -> Unit,
    val withdrawal: () -> Unit,
)