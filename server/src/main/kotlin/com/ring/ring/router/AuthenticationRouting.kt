package com.ring.ring.router

import com.ring.ring.controller.AuthenticationController
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authenticationRouting(
    controller: AuthenticationController = AuthenticationController()
) {
    route("") {
        post("signup") { controller.signUp(call) }
        post("login") { controller.login(call) }
    }
}