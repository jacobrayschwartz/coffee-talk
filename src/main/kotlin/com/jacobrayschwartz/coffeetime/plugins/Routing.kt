package com.jacobrayschwartz.coffeetime.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*

fun Application.configureRouting() {
    routing {
        singlePageApplication {
            react("react-app/out")
        }


        get("/api/hello") {
            call.respondText("Hello World!")
        }
    }
}
