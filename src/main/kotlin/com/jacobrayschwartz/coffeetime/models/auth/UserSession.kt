package com.jacobrayschwartz.coffeetime.models.auth

import io.ktor.server.auth.*

data class UserSession(
    val username: String,
    val accessToken: String,
    val name: String,
    val oidcIss : String,
    val oidcSub : String) : Principal