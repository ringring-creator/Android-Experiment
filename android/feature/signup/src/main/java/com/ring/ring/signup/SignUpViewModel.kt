package com.ring.ring.signup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel : ViewModel() {
    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setEmail(email: String) {
    }

    fun setPassword(password: String) {
    }

    fun signUp() {
    }
}