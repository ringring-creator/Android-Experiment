package com.ring.ring.plugin

import com.ring.ring.exception.BadRequestException
import com.ring.ring.exception.ConflictException
import com.ring.ring.exception.ForbiddenException
import com.ring.ring.exception.UnauthorizedException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(
    val message: String,
)

suspend fun ApplicationCall.respondError(
    httpStatusCode: HttpStatusCode,
    e: Throwable
) {
    respond(httpStatusCode, ErrorMessage(e.message ?: "Unknown"))
}

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is BadRequestException -> call.respondError(HttpStatusCode.BadRequest, cause)
                is UnauthorizedException -> call.respondError(HttpStatusCode.Unauthorized, cause)
                is ForbiddenException -> call.respondError(HttpStatusCode.Forbidden, cause)
                is ConflictException -> call.respondError(HttpStatusCode.Conflict, cause)
                else -> {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        ErrorMessage(cause.message ?: "Unknown error")
                    )
                }
            }
        }
    }
}
