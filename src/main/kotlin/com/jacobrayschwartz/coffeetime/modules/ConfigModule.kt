package com.jacobrayschwartz.coffeetime.modules

import com.jacobrayschwartz.coffeetime.data.Database
import com.jacobrayschwartz.coffeetime.data.initializeH2Database
import com.jacobrayschwartz.coffeetime.data.initializePostgresDatabase
import com.jacobrayschwartz.coffeetime.plugins.DatabaseConfig
import com.jacobrayschwartz.coffeetime.plugins.DatabaseTypes
import com.jacobrayschwartz.coffeetime.plugins.databaseConfigReader
import io.ktor.client.*
import io.ktor.server.config.*
import org.koin.core.module.Module
import org.koin.dsl.module


fun buildConfigModule(applicationConfig: ApplicationConfig) : Module {
    return module { single<ApplicationConfig>{ applicationConfig } }
}

fun buildHttpClientModule(httpClient: HttpClient) : Module {
    return module { single<HttpClient>{ httpClient } }
}

fun buildDatabaseModule(applicationConfig: ApplicationConfig) : Module {
    val databaseConfig = databaseConfigReader(applicationConfig)

    return when(databaseConfig.databaseType){
        DatabaseTypes.Postgres -> module { single<Database>{ initializePostgresDatabase(databaseConfig.postgresConfig!!) } }
        DatabaseTypes.Mysql -> TODO()
        DatabaseTypes.H2 -> module { single<Database>{ initializeH2Database() } }
    }
}