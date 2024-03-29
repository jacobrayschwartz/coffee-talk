package com.jacobrayschwartz.coffeetime.plugins

import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureHTTP() {
    routing {
        openAPI(path = "openapi")
    }
}
