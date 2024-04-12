package com.ring.ring.exception

class AuthorizationException(
    message: String? = null,
    throwable: Throwable? = null
) : Throwable(message, throwable)