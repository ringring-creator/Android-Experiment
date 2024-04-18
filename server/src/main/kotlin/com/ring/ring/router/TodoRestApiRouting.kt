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
    route("/todo") {
        authenticate("auth-jwt") {
            get("get/{todoId}") { controller.get(call) }
            get("list") { controller.list(call) }
            post("create") { controller.create(call) }
            put("edit") { controller.edit(call) }
            delete("delete/{todoId}") { controller.delete(call) }
            patch("edit-done") { controller.editDone(call) }
        }
    }
}