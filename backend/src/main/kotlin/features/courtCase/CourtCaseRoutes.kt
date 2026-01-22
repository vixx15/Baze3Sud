package com.baze3.features.courtCase

import com.baze3.models.ApiResponse
import com.baze3.models.CourtCaseDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.courtCaseRoutes(service: CourtCaseService) {
    route("/cases") {

        get {
            call.respond(service.getAllCases())
        }

        get("/details") {
            val id = call.request.queryParameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, ApiResponse<Unit>(false, "Broj predmeta nedostaje")
            )
            call.respond(service.getCaseDetails(id))
        }

        post {
            val dto = call.receive<CourtCaseDTO>()
            call.respond(service.createCase(dto))
        }

        put {
            val dto = call.receive<CourtCaseDTO>()
            call.respond(service.updateCase(dto))
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest, ApiResponse<Unit>(false, "Broj predmeta nedostaje")
            )
            call.respond(service.deleteCase(id))
        }
    }
}