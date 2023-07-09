package com.jacobrayschwartz.coffeetime.models.auth

import kotlinx.datetime.Instant
import java.util.*

/**
 * Represents a group, which can contain users by way of roles
 * @param id ID of the group
 * @param name User defined name of the group
 * @param createdByUserUUID ID of user who created the group
 * @param createdAt When the group was created
 * @param modifiedAt When the group was last updated
 */
data class Group(
    val id: Int,
    val name: String,
    val createdByUserUUID: UUID,
    val createdAt: Instant,
    val modifiedAt: Instant,
)