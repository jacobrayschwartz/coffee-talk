package com.jacobrayschwartz.coffeetime.modules

import com.jacobrayschwartz.coffeetime.modules.security.OktaSecurityProvider
import com.jacobrayschwartz.coffeetime.modules.security.SecurityProvider
import com.jacobrayschwartz.coffeetime.security.oktaConfigReader
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.module.Module
import org.koin.dsl.module


fun buildSecurityModule(applicationConfig: ApplicationConfig) = module{
    if(applicationConfig.propertyOrNull("security.okta") != null){
        single<SecurityProvider> { OktaSecurityProvider(oktaConfigReader(applicationConfig), get()) }
    }
}