package com.ring.ring.controller

import com.ring.ring.exception.UnauthorizedException
import com.ring.ring.usecase.user.EditUser
import com.ring.ring.usecase.user.GetUser
import com.ring.ring.usecase.user.WithdrawalUser
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class UserController(
    private val getUser: GetUser = GetUser(),
    private val editUser: EditUser = EditUser(),
    private val withdrawalUser: WithdrawalUser = WithdrawalUser(),
) {
    suspend fun get(call: ApplicationCall) {
        when (getVersion(call)) {
            1 -> getV1(call)
        }
    }

    private suspend fun getV1(call: ApplicationCall) {
        val res = getUser(
            req = GetUser.Req(
                email = receiveEmail(call)
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
        editUser(
            req = EditUser.Req(
                currentEmail = receiveEmail(call),
                body = call.receive<EditUser.Req.Body>()
            )
        )
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun withdrawal(call: ApplicationCall) {
        when (getVersion(call)) {
            1 -> withdrawalV1(call)
        }
    }

    private suspend fun withdrawalV1(call: ApplicationCall) {
        withdrawalUser(
            req = WithdrawalUser.Req(
                email = receiveEmail(call),
            )
        )
        call.respond(HttpStatusCode.NoContent)
    }

    private fun getVersion(call: ApplicationCall) =
        call.request.headers["API-Version"]?.toIntOrNull() ?: 1

    private fun receiveEmail(call: ApplicationCall): String {
        val principal = call.principal<JWTPrincipal>()
            ?: throw UnauthorizedException("Not logged in")
        return principal.payload.getClaim("email").asString()
    }
}