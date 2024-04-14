package com.ring.ring.todo.feature.edit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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

internal fun showRetrySnackbar(
    snackBarHostState: SnackbarHostState,
    message: String,
    actionLabel: String,
    action: () -> Unit,
    scope: CoroutineScope,
) {
    scope.launch {
        val snackBarResult = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = true,
        )
        when (snackBarResult) {
            SnackbarResult.Dismissed -> {}
            SnackbarResult.ActionPerformed -> {
                action()
            }
        }
    }
}