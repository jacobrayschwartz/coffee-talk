package com.jacobrayschwartz.coffeetime.data

import com.jacobrayschwartz.coffeetime.models.auth.Login
import com.jacobrayschwartz.coffeetime.models.auth.User
import java.util.UUID

interface LoginRespository {
    fun addAttemptedLogin(login: Login)

    fun getLoginsForUser(userUUID: UUID): List<Login>
}

interface UserRepository {
    fun upsertUser(user: User): User

    fun getUser(userUUID: UUID): User?

    fun getUsers(vararg userUUIDs: UUID): List<User>

    fun disableUser(userUUID: UUID)
}