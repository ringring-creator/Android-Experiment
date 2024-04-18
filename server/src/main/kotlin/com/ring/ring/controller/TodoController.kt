package com.ring.ring.controller

import com.ring.ring.exception.NotLoggedInException
import com.ring.ring.usecase.todo.CreateTodo
import com.ring.ring.usecase.todo.DeleteTodo
import com.ring.ring.usecase.todo.EditTodo
import com.ring.ring.usecase.todo.EditTodoDone
import com.ring.ring.usecase.todo.GetTodo
import com.ring.ring.usecase.todo.GetTodoList
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class TodoController(
    private val createTodo: CreateTodo = CreateTodo(),
    private val getTodo: GetTodo = GetTodo(),
    private val getTodoList: GetTodoList = GetTodoList(),
    private val editTodo: EditTodo = EditTodo(),
    private val deleteTodo: DeleteTodo = DeleteTodo(),
    private val editTodoDone: EditTodoDone = EditTodoDone(),
) {
    suspend fun create(call: ApplicationCall) {
        createTodo(
            req = CreateTodo.Req(
                todoReq = call.receive<CreateTodo.Req.Body>(),
                email = receiveEmail(call),
            )
        )
        call.respond(HttpStatusCode.OK)
    }

    suspend fun get(call: ApplicationCall) {
        val res = getTodo(
            req = GetTodo.Req(
                todoId = getTodoId(call),
                email = receiveEmail(call),
            )
        )
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun list(call: ApplicationCall) {
        val res = getTodoList(
            req = GetTodoList.Req(
                email = receiveEmail(call),
            )
        )
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun edit(call: ApplicationCall) {
        editTodo(
            req = EditTodo.Req(
                todo = call.receive<EditTodo.Req.Body>(),
                email = receiveEmail(call)
            )
        )
        call.respond(HttpStatusCode.OK)
    }

    suspend fun delete(call: ApplicationCall) {
        deleteTodo(
            req = DeleteTodo.Req(
                todoId = getTodoId(call),
                email = receiveEmail(call)
            )
        )
        call.respond(HttpStatusCode.OK)
    }

    private fun getTodoId(call: ApplicationCall) =
        call.parameters["todoId"]?.toLongOrNull() ?: throw IllegalArgumentException()

    suspend fun editDone(call: ApplicationCall) {
        val req = call.receive<EditTodoDone.Req>()
        editTodoDone(req = req)
        call.respond(HttpStatusCode.OK)
    }

    private fun receiveEmail(call: ApplicationCall): String {
        val principal = call.principal<JWTPrincipal>() ?: throw NotLoggedInException()
        return principal.payload.getClaim("email").asString()
    }
}