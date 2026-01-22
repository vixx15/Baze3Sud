package com.baze3.features.fristJudgementStatus

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.statusRoutes(statusService: FirstJudgementStatusService) {
    route("/judgement-statuses") {
        get {
            val response = statusService.getAllStatuses()
            call.respond(response)
        }
    }
}