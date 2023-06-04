package com.jacobrayschwartz.coffeetime.plugins

import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection(configuration: ApplicationConfig){
    install(Koin) {
        slf4jLogger()
    }
}