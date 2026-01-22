package com.baze3.common.db

import javax.sql.DataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DataSourceProvider {
    val dataSource: DataSource by lazy {
        HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = "jdbc:oracle:thin:@//localhost:49161/XEPDB1"
                username = "baze3"
                password = "baze"
                maximumPoolSize = 10
                minimumIdle = 2
                idleTimeout = 30_000
                connectionTimeout = 10_000
                poolName = "OracleHikariPool"
            }
        )
    }
}