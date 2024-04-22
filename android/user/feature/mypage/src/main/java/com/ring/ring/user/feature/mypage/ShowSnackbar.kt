package com.ring.ring.user.feature.mypage

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun showSnackbar(
    snackBarHostState: SnackbarHostState,
    message: String,
    scope: CoroutineScope
) {
    scope.launch {
        snackBarHostState.showSnackbar(
            message = message,
            withDismissAction = true,
        )
    }
}