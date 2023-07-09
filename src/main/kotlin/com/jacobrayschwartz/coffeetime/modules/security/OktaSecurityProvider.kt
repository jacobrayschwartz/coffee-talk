package com.jacobrayschwartz.coffeetime.modules.security

import com.jacobrayschwartz.coffeetime.models.auth.User
import com.jacobrayschwartz.coffeetime.models.auth.UserSession
import com.jacobrayschwartz.coffeetime.security.OktaConfig
import com.okta.jwt.AccessTokenVerifier
import com.okta.jwt.IdTokenVerifier
import com.okta.jwt.JwtVerifiers
import io.ktor.client.*
import io.ktor.server.auth.*

class OktaSecurityProvider(private val config: OktaConfig, private val httpClient: HttpClient) : SecurityProvider {

    private var idVerifier: IdTokenVerifier = JwtVerifiers.idTokenVerifierBuilder()
        .setClientId(config.clientId)
        .setIssuer(config.orgUrl)
        .build()

    private var accessTokenVerifier: AccessTokenVerifier = JwtVerifiers.accessTokenVerifierBuilder()
        .setAudience(config.audience)
        .setIssuer(config.orgUrl)
        .build()

    override fun getUserModel(session: UserSession): User {
        TODO("Not yet implemented")
    }

    override fun buildLogoutRedirect(session: UserSession) : String {
        return "${config.logoutUrl}?id_token_hint=${session.accessToken}"
    }

    override fun login(principal: OAuthAccessTokenResponse.OAuth2): UserSession {
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

        return UserSession(
            username = idToken.claims["preferred_username"]?.toString() ?: fullName.replace(
                "[^a-zA-Z0-9]".toRegex(),
                ""
            ),
            accessToken = idTokenString,
            name = fullName,
            oidcIss = idToken.claims["iss"]?.toString() ?: throw Exception("OIDC claim missing iss"),
            oidcSub = idToken.claims["sub"]?.toString() ?: throw Exception("OIDC claim missing sub")
        )
    }

}