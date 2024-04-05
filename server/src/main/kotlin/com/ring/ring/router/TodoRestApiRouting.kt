package com.ring.ring.router

import com.ring.ring.controller.TodoController
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.todoRouting(
    controller: TodoController = TodoController(),
) {
    route("/todo") {
        authenticate("auth-jwt") {
        post("create") { controller.create(call) }
            post("get") { controller.get(call) }
            post("list") { controller.list(call) }
            post("edit") { controller.edit(call) }
            post("delete") { controller.delete(call) }
            post("editDone") { controller.editDone(call) }
        }
    }
}