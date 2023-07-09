package com.jacobrayschwartz.coffeetime.models.auth

import kotlinx.datetime.Instant
import java.util.*

/**
 * Database model representing a login attempt by a user
 * @param userId ID of user attempting access
 * @param attemptedAt Time of attempted access
 * @param isSuccessful If attempt was successful
 */
data class Login(val userId: Long, val attemptedAt: Instant, val isSuccessful: Boolean)



