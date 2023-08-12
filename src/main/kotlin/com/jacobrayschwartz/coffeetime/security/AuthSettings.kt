package com.jacobrayschwartz.coffeetime.security

import io.ktor.server.config.*

fun oktaConfigReader(config: ApplicationConfig): OktaConfig = OktaConfig(
    orgUrl = config.tryGetString("security.okta.orgUrl") ?: throw IllegalArgumentException("security.okta.orgUrl must be specified"),
    clientId = config.tryGetString("security.okta.clientId") ?: throw IllegalArgumentException("security.okta.clientId must be specified"),
    clientSecret = config.tryGetString("security.okta.clientSecret") ?: throw IllegalArgumentException("security.okta.clientSecret must be specified"),
    audience = config.tryGetString("security.okta.audience") ?: "api://default"
)
