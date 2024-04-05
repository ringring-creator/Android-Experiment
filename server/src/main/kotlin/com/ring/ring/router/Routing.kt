package com.ring.ring.router

import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        userRestApiRouting()
        sessionRestApiRouting()
        todoRestApiRouting()
    }
}