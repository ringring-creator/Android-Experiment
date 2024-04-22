package com.ring.ring.user.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.network.exception.UnauthorizedException
import com.ring.ring.user.infra.model.Email
import com.ring.ring.user.infra.model.Password
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MyPageViewModel @Inject constructor(
    private val repository: MyPageRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(defaultMyPageUiState())
    val uiState = _uiState.asStateFlow()
    private val _event = Channel<MyPageEvent>(capacity = 5, BufferOverflow.DROP_OLDEST)
    val event = _event.receiveAsFlow()

    private val editErrorHandler = CoroutineExceptionHandler { _, th ->
        if (th is UnauthorizedException) {
            _event.trySend(MyPageEvent.UnauthorizedError)
            return@CoroutineExceptionHandler
        }
        _event.trySend(MyPageEvent.EditError)
    }
    private val withdrawalErrorHandler = CoroutineExceptionHandler { _, th ->
        if (th is UnauthorizedException) {
            _event.trySend(MyPageEvent.UnauthorizedError)
            return@CoroutineExceptionHandler
        }
        _event.trySend(MyPageEvent.WithdrawalError)
    }
    private val logoutErrorHandler = CoroutineExceptionHandler { _, th ->
        _event.trySend(MyPageEvent.LogoutError)
    }

    fun getUser() {
        viewModelScope.launch {
            repository.getUser()?.let {
                _uiState.value = uiState.value.replaceEmail(it.email.value)
            }
        }
    }

    fun setEmail(email: String) {
        _uiState.value = uiState.value.replaceEmail(email)
    }

    fun setPassword(password: String) {
        _uiState.value = uiState.value.replacePassword(password)
    }

    fun logout() {
        viewModelScope.launch(logoutErrorHandler) {
            repository.logout()
            _event.trySend(MyPageEvent.LogoutSuccess)
        }
    }

    fun edit() {
        viewModelScope.launch(editErrorHandler) {
            repository.edit(
                uiState.value.email.value,
                uiState.value.password.value
            )
            _event.trySend(MyPageEvent.EditSuccess)
        }
    }

    fun withdrawal() {
        viewModelScope.launch(withdrawalErrorHandler) {
            repository.withdrawal()
            _event.trySend(MyPageEvent.WithdrawalSuccess)
        }
    }

    private fun defaultMyPageUiState(): MyPageUiState {
        return MyPageUiState(
            email = MyPageUiState.Email("", isError = false, isShowSupportingText = false),
            password = MyPageUiState.Password("", isError = false),
            expandedAction = false,
        )
    }

    fun setExpandedAction(expanded: Boolean) {
        _uiState.value = uiState.value.copy(expandedAction = expanded)
    }
}

private fun MyPageUiState.replaceEmail(value: String): MyPageUiState {
    return this.copy(
        email = MyPageUiState.Email(
            value = value,
            isError = Email.isInvalidEmail(value),
            isShowSupportingText = false,
        ),
    )
}

private fun MyPageUiState.replacePassword(value: String): MyPageUiState {
    return this.copy(
        password = MyPageUiState.Password(
            value = value,
            isError = Password.isInvalidPassword(value),
        ),
    )
}