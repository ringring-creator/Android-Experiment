package com.ring.ring.user.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.network.exception.ConflictException
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
internal class SignUpViewModel @Inject constructor(
    private val userRepository: SignUpRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(defaultSignUpUiState())
    val uiState = _uiState.asStateFlow()
    private val _event = Channel<SignUpEvent>(capacity = 5, BufferOverflow.DROP_OLDEST)
    val event = _event.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, th ->
        if (th is ConflictException) {
            _uiState.value = uiState.value.replaceIsShowSupportText(true)
        }
        _event.trySend(SignUpEvent.SignUpError)
    }

    fun setEmail(email: String) {
        if (this.uiState.value.email.value == email) return
        _uiState.value = uiState.value.replaceEmail(email)
    }

    fun setPassword(password: String) {
        if (this.uiState.value.password.value == password) return
        _uiState.value = uiState.value.replacePassword(password)
    }

    fun signUp() {
        viewModelScope.launch(handler) {
            userRepository.signUp(uiState.value.email.value, uiState.value.password.value)
            _event.trySend(SignUpEvent.SignUpSuccess)
        }
    }

    private fun defaultSignUpUiState() = SignUpUiState(
        email = SignUpUiState.Email("", isError = false, isShowSupportingText = false),
        password = SignUpUiState.Password("", isError = false),
    )
}

private fun SignUpUiState.replaceEmail(value: String): SignUpUiState {
    return this.copy(
        email = SignUpUiState.Email(
            value = value,
            isError = Email.isInvalidEmail(value),
            isShowSupportingText = false,
        ),
    )
}

private fun SignUpUiState.replaceIsShowSupportText(value: Boolean): SignUpUiState {
    return this.copy(
        email = SignUpUiState.Email(
            value = this.email.value,
            isError = value,
            isShowSupportingText = value,
        ),
    )
}

private fun SignUpUiState.replacePassword(value: String): SignUpUiState {
    return this.copy(
        password = SignUpUiState.Password(
            value = value,
            isError = Password.isInvalidPassword(value),
        ),
    )
}