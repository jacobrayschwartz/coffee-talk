package com.jacobrayschwartz.coffeetime.plugins

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.*
import java.sql.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.routing.*


data class PostgresConfig(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val database: String,
    val schema: String?,
    val socketTimeout: Int = 3600
)

data class MysqlConfig(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val database: String
)

enum class DatabaseTypes { Postgres, Mysql, H2 }

data class DatabaseConfig(
    val databaseType: DatabaseTypes,
    val postgresConfig: PostgresConfig? = null,
    val mysqlConfig: MysqlConfig? = null
)

private const val CONFIG_KEY_DB_POSTGRES = "db.postgres"
private const val CONFIG_KEY_DB_MYSQL = "db.mysql"
private const val CONFIG_KEY_DB_H2 = "db.h2"

fun databaseConfigReader(config: ApplicationConfig): DatabaseConfig {
    val dbConnectionPropNames = arrayOf(CONFIG_KEY_DB_POSTGRES, CONFIG_KEY_DB_MYSQL, CONFIG_KEY_DB_MYSQL)

    val dbConnectionProps = dbConnectionPropNames.mapNotNull {
        val props = config.propertyOrNull(it)
        if(props != null){
            return@mapNotNull Pair(it, props)
        } else {
            null
        }
    }

    if(dbConnectionProps.isEmpty()){
        throw Exception("Database connection must be provided in config. Specify one of ${dbConnectionPropNames.joinToString(", ")}")
    }
    else if(dbConnectionProps.size > 1){
        throw Exception("Only one database connection can be provided. Found ${dbConnectionProps.joinToString(", ")}")
    }

    val connectionInfo = dbConnectionProps[0]
    when(connectionInfo.first){
        CONFIG_KEY_DB_POSTGRES -> return DatabaseConfig(databaseType = DatabaseTypes.Postgres, postgresConfig = postgresConfigReader(config))
        else -> throw NotImplementedError("Database connection for ${connectionInfo.first} has not been implemented yet")
    }
}

fun postgresConfigReader(config: ApplicationConfig): PostgresConfig{
    return PostgresConfig(
        host = config.tryGetString("$CONFIG_KEY_DB_POSTGRES.host") ?: throw IllegalArgumentException("$CONFIG_KEY_DB_POSTGRES.host is required"),
        port = config.tryGetString("$CONFIG_KEY_DB_POSTGRES.port")?.toIntOrNull() ?: throw IllegalArgumentException("$CONFIG_KEY_DB_POSTGRES.port is required"),
        database = config.tryGetString("$CONFIG_KEY_DB_POSTGRES.database") ?: throw IllegalArgumentException("$CONFIG_KEY_DB_POSTGRES.database is required"),
        user = config.tryGetString("$CONFIG_KEY_DB_POSTGRES.user") ?: throw IllegalArgumentException("$CONFIG_KEY_DB_POSTGRES.user is required"),
        password = config.tryGetString("$CONFIG_KEY_DB_POSTGRES.password") ?: throw IllegalArgumentException("$CONFIG_KEY_DB_POSTGRES.password is required"),
        schema = config.tryGetString("$CONFIG_KEY_DB_POSTGRES.schema"),
        socketTimeout = config.tryGetString("$CONFIG_KEY_DB_POSTGRES.socketTimeout")?.toIntOrNull() ?: 3600
    )
}
