package com.jacobrayschwartz.coffeetime.plugins

import com.jacobrayschwartz.coffeetime.security.asOAuth2Config
import com.jacobrayschwartz.coffeetime.security.oktaConfigReader
import com.okta.jwt.JwtVerifiers
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlin.collections.set

fun Application.configureSecurity(configuration: ApplicationConfig) {

    install(Sessions) {
        val secretEncryptKey = hex(configuration.tryGetString("security.sessionEncryptKey") ?: throw IllegalArgumentException("Session encrypt key is required"))
        val secretSignKey = hex(configuration.tryGetString("security.sessionSignKey") ?: throw IllegalArgumentException("Session sign key is required"))
        cookie<UserSession>("COFFEE_TIME_SESSION") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 10
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }

    if (configuration.propertyOrNull("security.okta") != null) {
        val oktaConfig = oktaConfigReader(configuration)
        val redirects = mutableMapOf<String, String>()

        val accessTokenVerifier = JwtVerifiers.accessTokenVerifierBuilder()
            .setAudience(oktaConfig.audience)
            .setIssuer(oktaConfig.orgUrl)
            .build()
        val idVerifier = JwtVerifiers.idTokenVerifierBuilder()
            .setClientId(oktaConfig.clientId)
            .setIssuer(oktaConfig.orgUrl)
            .build()
        authentication {

            oauth("okta") {
                urlProvider = { "http://localhost:8080/login/authorization-callback" }
                providerLookup = { oktaConfig.asOAuth2Config{ call, state ->
                    redirects[state] = call.request.queryParameters["redirectUrl"] ?: "/"
                } }
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
                    val accessToken = accessTokenVerifier.decode(principal.accessToken)
                    // Get idTokenString, parse and verify id token
                    val idTokenString = principal.extraParameters["id_token"]
                        ?: throw Exception("id_token wasn't returned")
                    val idToken = idVerifier.decode(idTokenString, null)
                    // Try to get handle from the id token, of failback to subject field in access token
                    val fullName = (idToken.claims["name"] ?: accessToken.claims["sub"] ?: "UNKNOWN_NAME").toString()
                    println("User $fullName logged in successfully")
                    // Create a session object with "slugified" username
                    val session = UserSession(
                        username = idToken.claims["preferred_username"]?.toString() ?: fullName.replace("[^a-zA-Z0-9]".toRegex(), ""),
                        accessToken = idTokenString,
                        name = fullName
                    )

                    call.sessions.set(session)

                    val redirect = redirects[principal.state!!]
                    call.respondRedirect(redirect!!)
                }
            }
        }
    }
}

data class UserSession(val username: String, val accessToken: String, val name: String)
