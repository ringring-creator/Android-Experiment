package com.ring.ring.user.feature.login

sealed class LoginEvent {
    object LoginSuccess : LoginEvent()
    object LoginError : LoginEvent()
}