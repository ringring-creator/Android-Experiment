package com.ring.ring.user.feature.login

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
internal class LoginViewModel @Inject constructor(
    private val userRepository: LoginRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState("", ""))
    val uiState = _uiState.asStateFlow()

    private val _loginFinishedEvent = Channel<Unit>()
    val loginFinishedEvent = _loginFinishedEvent.receiveAsFlow()
    private val _loginFailedEvent = Channel<Unit>()
    val loginFailedEvent = _loginFailedEvent.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, _ ->
        _loginFailedEvent.trySend(Unit)
    }

    fun setEmail(email: String) {
        if (this.uiState.value.email == email) return
        _uiState.value = uiState.value.copy(email = email)
    }

    fun setPassword(password: String) {
        if (this.uiState.value.password == password) return
        _uiState.value = uiState.value.copy(password = password)
    }

    fun login() {
        viewModelScope.launch(handler) {
            userRepository.login(uiState.value.email, uiState.value.password)
            _loginFinishedEvent.trySend(Unit)
        }
    }
}