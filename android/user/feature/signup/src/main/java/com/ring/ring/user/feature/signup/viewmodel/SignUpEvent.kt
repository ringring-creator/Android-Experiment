package com.ring.ring.user.feature.signup.viewmodel

sealed class SignUpEvent {
    object SignUpSuccess : SignUpEvent()
    object SignUpError : SignUpEvent()
}