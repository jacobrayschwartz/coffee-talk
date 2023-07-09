package com.jacobrayschwartz.coffeetime.modules

import com.jacobrayschwartz.coffeetime.plugins.UserSession
import com.jacobrayschwartz.coffeetime.security.OktaConfig
import com.jacobrayschwartz.coffeetime.security.oktaConfigReader
import com.okta.jwt.AccessTokenVerifier
import com.okta.jwt.IdTokenVerifier
import com.okta.jwt.JwtVerifiers
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

interface SecurityProvider{
    fun getUserInfo(session: UserSession): UserInfo

    fun login(principal: OAuthAccessTokenResponse.OAuth2): UserSession

    fun buildLogoutRedirect(userSession: UserSession): String
}

class OktaSecurityProvider(private val config: OktaConfig, private val httpClient: HttpClient) : SecurityProvider{

    private var idVerifier: IdTokenVerifier = JwtVerifiers.idTokenVerifierBuilder()
        .setClientId(config.clientId)
        .setIssuer(config.orgUrl)
        .build()

    private var accessTokenVerifier: AccessTokenVerifier = JwtVerifiers.accessTokenVerifierBuilder()
        .setAudience(config.audience)
        .setIssuer(config.orgUrl)
        .build()

    override fun getUserInfo(session: UserSession): UserInfo {
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
            name = fullName
        )
    }

}


@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    @SerialName("given_name") val givenName: String,
    @SerialName("family_name") val familyName: String,
    val picture: String,
    val locale: String
)

fun buildConfigModule(applicationConfig: ApplicationConfig) : Module{
    return module { single<ApplicationConfig>{ applicationConfig } }
}

fun buildHttpClientModule(httpClient: HttpClient) : Module{
    return module { single<HttpClient>{ httpClient } }
}

fun buildSecurityModule(applicationConfig: ApplicationConfig) = module{
    if(applicationConfig.propertyOrNull("security.okta") != null){
        single<SecurityProvider> { OktaSecurityProvider(oktaConfigReader(applicationConfig), get()) }
    }
}