package com.jacobrayschwartz.coffeetime.modules

import io.ktor.client.*
import io.ktor.server.config.*
import org.koin.core.module.Module
import org.koin.dsl.module


fun buildConfigModule(applicationConfig: ApplicationConfig) : Module {
    return module { single<ApplicationConfig>{ applicationConfig } }
}

fun buildHttpClientModule(httpClient: HttpClient) : Module {
    return module { single<HttpClient>{ httpClient } }
}