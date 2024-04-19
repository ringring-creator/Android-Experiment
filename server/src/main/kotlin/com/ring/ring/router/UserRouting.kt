package com.ring.ring.router

import com.ring.ring.controller.UserController
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.userRouting(
    controller: UserController = UserController()
) {
    route("/users") {
        authenticate("auth-jwt") {
            get("") { controller.get(call) }
            put("") { controller.edit(call) }
            delete("") { controller.withdrawal(call) }
        }
    }
}