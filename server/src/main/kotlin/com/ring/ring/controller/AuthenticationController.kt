package com.ring.ring.controller

import com.ring.ring.usecase.user.Login
import com.ring.ring.usecase.user.SignUp
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class AuthenticationController(
    private val login: Login = Login(),
    private val signUp: SignUp = SignUp(),
) {
    suspend fun login(call: ApplicationCall) {
        when (getVersion(call)) {
            1 -> loginV1(call)
        }
    }

    private suspend fun loginV1(call: ApplicationCall) {
        val req = call.receive<Login.Req>()
        val res = login(req)
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun signUp(call: ApplicationCall) {
        when (getVersion(call)) {
            1 -> signUpV1(call)
        }
    }

    private suspend fun signUpV1(call: ApplicationCall) {
        val req = call.receive<SignUp.Req>()
        signUp(req)
        call.respond(HttpStatusCode.OK)
    }

    private fun getVersion(call: ApplicationCall) =
        call.request.headers["API-Version"]?.toIntOrNull() ?: 1
}