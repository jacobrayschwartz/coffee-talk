package com.jacobrayschwartz.coffeetime.models.auth

import kotlinx.datetime.Instant

/**
 * Database model for tracking users - not to be confused with the session model
 * @see com.jacobrayschwartz.coffeetime.plugins.UserSession for session
 * @param id ID of user (internal to app, not used by OIDC)
 * @param oidcIss Issuer of the user's OIDC token
 * @param oidcSub ID of the user from OIDC (combine with oidcIss for unique ID)
 * @param userEmail Email address for user
 * @param provider OAuth provider type
 * @param displayName Display name for user
 * @param createdAt User created at time
 * @param modifiedAt User modified time
 * @param roles Roles user has access to
 */
data class User(
    val id: Long,
    val oidcIss: String,
    val oidcSub: String,
    val userEmail: String,
    val provider: String,
    val displayName: String,
    val createdAt: Instant,
    val modifiedAt: Instant,
    val roles: List<Role>
)