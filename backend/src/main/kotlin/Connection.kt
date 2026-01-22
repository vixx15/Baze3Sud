package com.baze3

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun createOracleConnection(): Connection? {
    //val jdbcUrl = "jdbc:oracle:thin:@localhost:49161/XEPDB1" // XE = Oracle Express
    val jdbcUrl = "jdbc:log4jdbc:oracle:thin:@localhost:49161/XEPDB1" // XE = Oracle Express
    val driverClassName = "net.sf.log4jdbc.sql.jdbcapi.DriverSpy"

    val username = "baze3"
    val password = "baze"

    return try {
        DriverManager.getConnection(jdbcUrl, username, password).also {
            println("Connected to Oracle database baze4 successfully!")
        }
    } catch (e: SQLException) {
        println("CONNECTION FIALED")
        e.printStackTrace()
        null
    }
}