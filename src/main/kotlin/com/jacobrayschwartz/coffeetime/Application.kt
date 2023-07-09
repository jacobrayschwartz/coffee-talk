package com.jacobrayschwartz.coffeetime

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.jacobrayschwartz.coffeetime.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.client.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

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
    configureDatabases()
    configureSockets()
    configureRouting()
}
