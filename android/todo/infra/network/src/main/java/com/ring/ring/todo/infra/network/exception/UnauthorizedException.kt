package com.ring.ring.todo.infra.network.exception

class UnauthorizedException(
    message: String? = null,
    throwable: Throwable? = null
) : Throwable(message, throwable)