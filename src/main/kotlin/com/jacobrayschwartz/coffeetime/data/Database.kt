package com.jacobrayschwartz.coffeetime.data

import com.jacobrayschwartz.coffeetime.plugins.PostgresConfig
import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.postgresql.ds.PGSimpleDataSource
import java.sql.DriverManager


class Database(val jdbi: Jdbi)

fun initializeH2Database(): Database{
    val h2 = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")

    val jdbi = Jdbi.create(h2)

    return Database(jdbi)
}

fun initializePostgresDatabase(config: PostgresConfig): Database{
    val pg = PGSimpleDataSource()
    pg.serverNames = arrayOf(config.host)
    pg.portNumbers = intArrayOf(config.port)
    pg.user = config.user
    pg.password = config.password
    pg.databaseName = config.database
    pg.socketTimeout = config.socketTimeout

    if(config.schema != null){
        pg.currentSchema = config.schema
    }

    val hk = HikariDataSource().apply {
        dataSource = pg
    }

    hk.minimumIdle = 2
    hk.leakDetectionThreshold = 20_000


    val jdbi = Jdbi.create(hk)
    jdbi.installPlugin(KotlinPlugin())
    jdbi.installPlugin(PostgresPlugin())
    return Database(jdbi);
}