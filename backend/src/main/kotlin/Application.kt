package com.baze3

import io.ktor.server.application.*

fun main(args: Array<String>) {
   // createOracleConnection()
    //io.ktor.server.netty.EngineMain.main(args)
    /*embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)*/
}

fun Application.module() {
    //createOracleConnection()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
