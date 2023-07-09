package com.jacobrayschwartz.coffeetime.plugins

import com.jacobrayschwartz.coffeetime.models.auth.UserSession
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*

fun Application.configureRouting() {
    routing {
        singlePageApplication {
            react("react-app/out")
        }


        get("/api/hello") {
            val userSession: UserSession? = call.sessions.get()
            if (userSession != null) {
                call.respondText("Hello, ${userSession.name}!")
            } else {
                val redirectUrl = URLBuilder("http://0.0.0.0:8080/login").run {
                    parameters.append("redirectUrl", call.request.uri)
                    build()
                }
                call.respondRedirect(redirectUrl)
            }
        }
    }
}
