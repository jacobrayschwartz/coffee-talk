package com.jacobrayschwartz.coffeetime.plugins

import com.jacobrayschwartz.coffeetime.models.auth.UserSession
import com.jacobrayschwartz.coffeetime.modules.security.SecurityProvider
import com.jacobrayschwartz.coffeetime.security.asOAuth2Config
import com.jacobrayschwartz.coffeetime.security.oktaConfigReader
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import org.koin.ktor.ext.inject
import kotlin.collections.set

fun Application.configureSecurity() {

    val configuration by inject<ApplicationConfig>()
    val securityProvider by inject<SecurityProvider>()

    install(Sessions) {
        val secretEncryptKey = hex(configuration.tryGetString("security.sessionEncryptKey") ?: throw IllegalArgumentException("Session encrypt key is required"))
        val secretSignKey = hex(configuration.tryGetString("security.sessionSignKey") ?: throw IllegalArgumentException("Session sign key is required"))
        cookie<UserSession>("COFFEE_TIME_SESSION") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }

    routing {
        get("logout") {
            val userSession : UserSession? = call.sessions.get<UserSession>()

            val logoutRedirect =
                if(userSession != null){
                    securityProvider.buildLogoutRedirect(userSession)
                } else{
                    "/"
                }

            call.sessions.clear<UserSession>()
            call.respondRedirect(logoutRedirect)
        }
    }

    if (configuration.propertyOrNull("security.okta") != null) {
        configureOkta(configuration, securityProvider)
    }
}

private fun Application.configureOkta(
    configuration: ApplicationConfig,
    securityProvider: SecurityProvider
) {
    val oktaConfig = oktaConfigReader(configuration)
    val redirects = mutableMapOf<String, String>()
    authentication {

        oauth("okta") {
            urlProvider = { "http://localhost:8080/login/authorization-callback" }
            providerLookup = {
                oktaConfig.asOAuth2Config { call, state ->
                    redirects[state] = call.request.queryParameters["redirectUrl"] ?: "/"
                }
            }
            client = HttpClient()
        }
    }
    routing {
        authenticate("okta") {

            get("login") {
                //call.respondRedirect("/login/authorization-callback")
            }

            get("login/authorization-callback") {
                // Get a principal from OAuth2 token
                val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                    ?: throw Exception("No principal was given")
                // Parse and verify access token with OktaJwtVerifier

                val session = securityProvider.login(principal)

                call.sessions.set(session)

                val redirect = redirects[principal.state!!]
                call.respondRedirect(redirect!!)
            }
        }
    }
}

