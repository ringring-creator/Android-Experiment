package com.ring.ring.router

import com.ring.ring.controller.UserController
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.userRouting(
    controller: UserController = UserController()
) {
    route("/user") {
        post("signup") { controller.signUp(call) }
        post("login") { controller.login(call) }
        authenticate("auth-jwt") {
            post("get") { controller.get(call) }
            post("edit") { controller.edit(call) }
            post("withdrawal") { controller.withdrawal(call) }
        }
    }
}