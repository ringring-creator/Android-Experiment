package com.ring.ring

import com.ring.ring.plugin.configureAuthentication
import com.ring.ring.plugin.configureCors
import com.ring.ring.plugin.configureSerialization
import com.ring.ring.plugin.configureStatusPages
import com.ring.ring.router.configureRouting
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureAuthentication()
    configureSerialization()
    configureCors()
    configureStatusPages()
    configureRouting()
}