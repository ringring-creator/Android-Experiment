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
        if (this.email.value == email) return
        _email.value = email
    }

    fun setPassword(password: String) {
        if (this.password.value == password) return
        _password.value = password
    }

    fun signUp() {
    }
}