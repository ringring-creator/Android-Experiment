package com.ring.ring.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: SignUpUserRepository,
) : ViewModel() {
    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password = _password.asStateFlow()
    private val _signUpFinishedEvent = Channel<Unit>()
    val signUpFinishedEvent = _signUpFinishedEvent.receiveAsFlow()


    fun setEmail(email: String) {
        if (this.email.value == email) return
        _email.value = email
    }

    fun setPassword(password: String) {
        if (this.password.value == password) return
        _password.value = password
    }

    fun signUp() {
        viewModelScope.launch {
            userRepository.signUp(email.value, password.value)
            _signUpFinishedEvent.trySend(Unit)
        }
    }
}