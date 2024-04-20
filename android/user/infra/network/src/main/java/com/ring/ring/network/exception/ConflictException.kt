package com.ring.ring.network.exception

class ConflictException(
    message: String? = null,
    throwable: Throwable? = null
) : Throwable(message, throwable)