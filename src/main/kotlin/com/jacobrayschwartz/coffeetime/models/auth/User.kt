package com.jacobrayschwartz.coffeetime.models.auth

import kotlinx.datetime.Instant
import java.util.*

/**
 * Database model for tracking users - not to be confused with the session model
 * @see com.jacobrayschwartz.coffeetime.plugins.UserSession for session
 * @param userUUID UUID of user provided from OAuth
 * @param userEmail Email address for user
 * @param provider OAuth provider type
 * @param displayName Display name for user
 * @param createdAt User created at time
 * @param modifiedAt User modified time
 * @param roles Roles user has access to
 */
data class User(
    val userUUID: UUID,
    val userEmail: String,
    val provider: String,
    val displayName: String,
    val createdAt: Instant,
    val modifiedAt: Instant,
    val roles: List<Role>
)