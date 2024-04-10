package com.ring.ring.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: LoginRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _loginFinishedEvent = Channel<Unit>()
    val loginFinishedEvent = _loginFinishedEvent.receiveAsFlow()
    private val _loginFailedEvent = Channel<Unit>()
    val loginFailedEvent = _loginFailedEvent.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, _ ->
        _loginFailedEvent.trySend(Unit)
    }

    fun setEmail(email: String) {
        if (this.email.value == email) return
        _email.value = email
    }

    fun setPassword(password: String) {
        if (this.password.value == password) return
        _password.value = password
    }

    fun login() {
        viewModelScope.launch(handler) {
            userRepository.login(email.value, password.value)
            _loginFinishedEvent.trySend(Unit)
        }
    }
}