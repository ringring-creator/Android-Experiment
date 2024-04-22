package com.ring.ring.user.feature.login.viewmodel

sealed class LoginEvent {
    object LoginSuccess : LoginEvent()
    object LoginError : LoginEvent()
}