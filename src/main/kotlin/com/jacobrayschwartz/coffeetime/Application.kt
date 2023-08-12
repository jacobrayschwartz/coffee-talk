package com.jacobrayschwartz.coffeetime

import io.ktor.server.application.*
import com.jacobrayschwartz.coffeetime.plugins.*
import io.ktor.client.*

//fun main() {
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)
//}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureDependencyInjection(environment.config, HttpClient())
    configureSecurity()
    //configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting()
}
