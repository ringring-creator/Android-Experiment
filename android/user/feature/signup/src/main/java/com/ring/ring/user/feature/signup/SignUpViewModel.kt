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
    private val _uiState = MutableStateFlow(initSignUpUiState)
    val uiState = _uiState.asStateFlow()
    private val _signUpFinishedEvent = Channel<Unit>()
    val signUpFinishedEvent = _signUpFinishedEvent.receiveAsFlow()
    private val _signUpFailedEvent = Channel<Unit>()
    val signUpFailedEvent = _signUpFailedEvent.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, _ ->
        _signUpFailedEvent.trySend(Unit)
    }

    fun setEmail(email: String) {
        if (this.uiState.value.email.input == email) return
        _uiState.value = uiState.value.copy(
            email = generateEmail(email)
        )
    }

    fun setPassword(password: String) {
        if (this.uiState.value.password.input == password) return
        _uiState.value = uiState.value.copy(
            password = generatePassword(password)
        )
    }

    fun signUp() {
        viewModelScope.launch(handler) {
            userRepository.signUp(uiState.value.email.input, uiState.value.password.input)
            _signUpFinishedEvent.trySend(Unit)
        }
    }

    private fun generateEmail(emailValue: String) = SignUpUiState.Email(
        input = emailValue,
        isError = Email.isInvalidEmail(emailValue),
    )

    private fun generatePassword(password: String) = SignUpUiState.Password(
        input = password,
        isError = Password.isInvalidPassword(password),
    )

    companion object {
        val initSignUpUiState = SignUpUiState(
            email = SignUpUiState.Email("", isError = false),
            password = SignUpUiState.Password("", isError = false),
        )
    }
}