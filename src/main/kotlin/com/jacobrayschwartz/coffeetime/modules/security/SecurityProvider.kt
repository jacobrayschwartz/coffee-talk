package com.jacobrayschwartz.coffeetime.modules.security

import com.jacobrayschwartz.coffeetime.models.auth.User
import com.jacobrayschwartz.coffeetime.models.auth.UserSession
import io.ktor.server.auth.*

interface SecurityProvider{
    fun getUserModel(session: UserSession): User

    fun login(principal: OAuthAccessTokenResponse.OAuth2): UserSession

    fun buildLogoutRedirect(userSession: UserSession): String
}