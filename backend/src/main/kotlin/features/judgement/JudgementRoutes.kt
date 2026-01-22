package com.baze3.features.judgement

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.judgementRoutes(service: JudgementService) {
    route("/judgements") {
        get {
            val response = service.getAllJudgements()
            call.respond(response)
        }
    }
}