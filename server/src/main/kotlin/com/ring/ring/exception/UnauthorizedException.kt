package com.ring.ring.exception

class UnauthorizedException(
    message: String? = null,
    throwable: Throwable? = null
) : Throwable(message, throwable)