package com.ring.ring.router

import com.ring.ring.controller.TodoController
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.todoRouting(
    controller: TodoController = TodoController(),
) {
    route("/todos") {
        authenticate("auth-jwt") {
            get("{todoId}") { controller.get(call) }
            get("") { controller.list(call) }
            post("") { controller.create(call) }
            put("{todoId}") { controller.edit(call) }
            delete("{todoId}") { controller.delete(call) }
            patch("edit-done/{todoId}") { controller.editDone(call) }
        }
    }
}