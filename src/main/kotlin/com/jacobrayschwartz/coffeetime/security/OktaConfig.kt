package com.jacobrayschwartz.coffeetime.security

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

data class OktaConfig(
    val orgUrl: String,
    val clientId: String,
    val clientSecret: String,
    val audience: String
) {
    val accessTokenUrl = "$orgUrl/v1/token"
    val authorizeUrl = "$orgUrl/v1/authorize"
    val logoutUrl = "$orgUrl/v1/logout"
}

fun OktaConfig.asOAuth2Config(onStateCreatedCallback: (ApplicationCall, String) -> Unit = { _, _ -> }):
        OAuthServerSettings.OAuth2ServerSettings {
    return OAuthServerSettings.OAuth2ServerSettings(
        name = "okta",
        authorizeUrl = authorizeUrl,
        accessTokenUrl = accessTokenUrl,
        clientId = clientId,
        clientSecret = clientSecret,
        defaultScopes = listOf("openid", "profile"),
        requestMethod = HttpMethod.Post,
        onStateCreated = onStateCreatedCallback
    )
}