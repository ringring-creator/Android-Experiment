package com.ring.ring.controller

import com.ring.ring.exception.BadRequestException
import com.ring.ring.exception.UnauthorizedException
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
        when (getVersion(call)) {
            1 -> createV1(call)
        }
    }

    private suspend fun createV1(call: ApplicationCall) {
        createTodo(
            req = CreateTodo.Req(
                todoReq = call.receive<CreateTodo.Req.Body>(),
                email = receiveEmail(call),
            )
        )
        call.respond(HttpStatusCode.Created)
    }

    suspend fun get(call: ApplicationCall) {
        when (getVersion(call)) {
            1 -> getV1(call)
        }
    }

    private suspend fun getV1(call: ApplicationCall) {
        val res = getTodo(
            req = GetTodo.Req(
                todoId = getTodoId(call),
                email = receiveEmail(call),
            )
        )
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun list(call: ApplicationCall) {
        when (getVersion(call)) {
            1 -> listV1(call)
        }
    }

    private suspend fun listV1(call: ApplicationCall) {
        val res = getTodoList(
            req = GetTodoList.Req(
                email = receiveEmail(call),
            )
        )
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun edit(call: ApplicationCall) {
        when (getVersion(call)) {
            1 -> editV1(call)
        }
    }

    private suspend fun editV1(call: ApplicationCall) {
        editTodo(
            req = EditTodo.Req(
                todoId = getTodoId(call),
                body = call.receive<EditTodo.Req.Body>(),
                email = receiveEmail(call)
            )
        )
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun delete(call: ApplicationCall) {
        when (getVersion(call)) {
            1 -> deleteV1(call)
        }
    }

    private suspend fun deleteV1(call: ApplicationCall) {
        deleteTodo(
            req = DeleteTodo.Req(
                todoId = getTodoId(call),
                email = receiveEmail(call)
            )
        )
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun editDone(call: ApplicationCall) {
        when (getVersion(call)) {
            1 -> editDoneV1(call)
        }
    }

    private suspend fun editDoneV1(call: ApplicationCall) {
        editTodoDone(
            req = EditTodoDone.Req(
                todoId = getTodoId(call),
                body = call.receive<EditTodoDone.Req.Body>(),
                email = receiveEmail(call),
            )
        )
        call.respond(HttpStatusCode.NoContent)
    }

    private fun getVersion(call: ApplicationCall) =
        call.request.headers["API-Version"]?.toIntOrNull() ?: 1

    private fun getTodoId(call: ApplicationCall) =
        call.parameters["todoId"]?.toLongOrNull()
            ?: throw BadRequestException("todoId is not found")

    private fun receiveEmail(call: ApplicationCall): String {
        val principal = call.principal<JWTPrincipal>()
            ?: throw UnauthorizedException("Not logged in")
        return principal.payload.getClaim("email").asString()
    }
}