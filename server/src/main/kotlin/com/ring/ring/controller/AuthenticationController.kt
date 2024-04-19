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
        val req = call.receive<Login.Req>()
        val res = login(req)
        call.respond(HttpStatusCode.OK, res)
    }

    suspend fun signUp(call: ApplicationCall) {
        try {
            val req = call.receive<SignUp.Req>()
            signUp(req)
            call.respond(HttpStatusCode.OK)
        } catch (e: Throwable) {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}