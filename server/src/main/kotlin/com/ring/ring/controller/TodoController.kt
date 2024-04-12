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
                todo = call.receive<CreateTodo.Req.ReqTodo>(),
                email = receiveEmail(call),
            )
        )
        call.respond(HttpStatusCode.OK)
    }

    suspend fun get(call: ApplicationCall) {
        val req = call.receive<GetTodo.Req>()
        val res = getTodo(req = req)
        call.respond(HttpStatusCode.OK, res.todo)
    }

    suspend fun list(call: ApplicationCall) {
        val req = call.receive<GetTodoList.Req>()
        val res = getTodoList(req = req)
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun edit(call: ApplicationCall) {
        val req = call.receive<EditTodo.Req>()
        editTodo(req = req)
        call.respond(HttpStatusCode.OK)
    }

    suspend fun delete(call: ApplicationCall) {
        val req = call.receive<DeleteTodo.Req>()
        deleteTodo(req = req)
        call.respond(HttpStatusCode.OK)
    }

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