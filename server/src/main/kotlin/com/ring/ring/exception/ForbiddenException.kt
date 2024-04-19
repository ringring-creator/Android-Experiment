package com.ring.ring.exception

class ForbiddenException(
    message: String? = null,
    throwable: Throwable? = null
) : Throwable(message, throwable)