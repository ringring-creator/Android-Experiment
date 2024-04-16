package com.ring.ring.user.feature.signup

sealed class SignUpEvent {
    object SignUpSuccess : SignUpEvent()
    object SignUpError : SignUpEvent()
}