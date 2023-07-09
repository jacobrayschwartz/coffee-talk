package com.jacobrayschwartz.coffeetime.models.auth

import kotlinx.datetime.Instant
import java.util.*


enum class RoleType {
    /**
     * Access to all admin functions across the site, can create, view and edit boards for all groups
     */
    SuperAdmin,

    /**
     * Access to admin functions for that group, can create, view and edit all boards for that group
     */
    GroupAdmin,

    /**
     * Can create, view and edit boards for that group
     */
    Standard
}

/**
 * @param id ID of the role
 * @param userUUID ID of the user this role belongs to
 * @param groupId ID of the group this role belongs to - may be null if applies to entire site
 * @param roleType Type of role/permission this user has for this group
 * @param createdAt When the group was created
 * @param modifiedAt When the group was last updated
 */
data class Role(
    val id: Long,
    val userUUID: UUID,
    val roleType: RoleType,
    val groupId: Int?,
    val createdAt: Instant,
    val modifiedAt: Instant
)