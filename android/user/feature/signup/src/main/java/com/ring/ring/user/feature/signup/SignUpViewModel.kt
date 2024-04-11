package com.ring.ring.user.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.user.feature.signup.SignUpUiState.Email
import com.ring.ring.user.feature.signup.SignUpUiState.Password
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
    private val _email = MutableStateFlow(initEmail)
    val email = _email.asStateFlow()
    private val _password = MutableStateFlow(initPassword)
    val password = _password.asStateFlow()
    private val _signUpFinishedEvent = Channel<Unit>()
    val signUpFinishedEvent = _signUpFinishedEvent.receiveAsFlow()
    private val _signUpFailedEvent = Channel<Unit>()
    val signUpFailedEvent = _signUpFailedEvent.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, _ ->
        _signUpFailedEvent.trySend(Unit)
    }

    fun setEmail(email: String) {
        if (this.email.value.input == email) return
        _email.value = generateEmail(email)
    }

    fun setPassword(password: String) {
        if (this.password.value.input == password) return
        _password.value = generatePassword(password)
    }

    fun signUp() {
        viewModelScope.launch(handler) {
            userRepository.signUp(email.value.input, password.value.input)
            _signUpFinishedEvent.trySend(Unit)
        }
    }

    private fun generateEmail(emailValue: String) = Email(
        input = emailValue,
        isError = isInvalidEmail(emailValue),
        visibleSupportingText = isInvalidEmail(emailValue)
    )

    private fun isInvalidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex).not()
    }

    private fun generatePassword(password: String) = Password(
        input = password,
        isError = isInvalidPassword(password),
        visibleSupportingText = isInvalidPassword(password),
    )

    private fun isInvalidPassword(password: String): Boolean {
        if (password.length < 8) return true

        var hasDigit = false
        var hasUpperCase = false
        var hasLowerCase = false

        for (char in password) {
            when {
                char.isDigit() -> hasDigit = true
                char.isUpperCase() -> hasUpperCase = true
                char.isLowerCase() -> hasLowerCase = true
            }
            if (hasDigit && hasUpperCase && hasLowerCase) return false
        }
        return true
    }

    companion object {
        val initEmail = Email("", isError = false, visibleSupportingText = false)
        val initPassword = Password("", isError = false, visibleSupportingText = false)
    }
}