package com.jacobrayschwartz.coffeetime.plugins

import com.jacobrayschwartz.coffeetime.modules.security.buildConfigModule
import com.jacobrayschwartz.coffeetime.modules.security.buildHttpClientModule
import com.jacobrayschwartz.coffeetime.modules.security.buildSecurityModule
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection(configuration: ApplicationConfig, httpClient: HttpClient){
    install(Koin) {
        slf4jLogger()
        modules(
            buildConfigModule(configuration),
            buildHttpClientModule(httpClient),
            buildSecurityModule(configuration)
        )
    }
}