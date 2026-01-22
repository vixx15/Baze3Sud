package com.baze3.features.disputeType

import com.baze3.models.ApiResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.disputeTypeRoutes(service: DisputeTypeService) {
    route("/dispute-types") {

        get {
            call.respond(service.getAllDisputeTypes())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse<Unit>(false, "Loš ID"))
                return@get
            }
            call.respond(service.getDisputeTypeById(id))
        }
    }
}