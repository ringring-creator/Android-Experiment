package com.ring.ring.user.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.user.infra.model.Email
import com.ring.ring.user.infra.model.Password
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
    private val _signUpFinishedEvent = Channel<Unit>()
    val signUpFinishedEvent = _signUpFinishedEvent.receiveAsFlow()
    private val _signUpFailedEvent = Channel<Unit>()
    val signUpFailedEvent = _signUpFailedEvent.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, _ ->
        _signUpFailedEvent.trySend(Unit)
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
            _signUpFinishedEvent.trySend(Unit)
        }
    }

    private fun defaultSignUpUiState() = SignUpUiState(
        email = SignUpUiState.Email("", isError = false),
        password = SignUpUiState.Password("", isError = false),
    )
}

private fun SignUpUiState.replaceEmail(value: String): SignUpUiState {
    return this.copy(
        email = SignUpUiState.Email(
            value = value,
            isError = Email.isInvalidEmail(value),
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