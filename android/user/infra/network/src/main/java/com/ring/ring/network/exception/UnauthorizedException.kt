package com.ring.ring.network.exception

class UnauthorizedException(
    message: String? = null,
    throwable: Throwable? = null
) : Throwable(message, throwable)