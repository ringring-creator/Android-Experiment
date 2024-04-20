package com.ring.ring.exception

class ConflictException(
    message: String? = null,
    throwable: Throwable? = null
) : Throwable(message, throwable)