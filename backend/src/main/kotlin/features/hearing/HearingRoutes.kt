package com.baze3.features.hearing

import com.baze3.models.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.hearingRoutes(service: HearingService) {
    route("/hearings") {

        get {
            call.respond(service.getAllHearings())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()

            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Nevalidan broj rasprave")
                )
                return@get
            }

            val response = service.getHearingById(id)

            if (response.success) {
                call.respond(response)
            } else {
                call.respond(HttpStatusCode.NotFound, response)
            }
        }
    }
}