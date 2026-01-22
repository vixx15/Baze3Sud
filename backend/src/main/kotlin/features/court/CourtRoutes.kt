package com.baze3.features.court

import com.baze3.models.ApiResponse
import com.baze3.models.CourtDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.courtRoutes(service: CourtService) {
    route("/courts") {

        get {
            call.respond(service.getAllCourts())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Neispravan ID suda")
                )
                return@get
            }

            val response = service.getCourtById(id)
            val status = if (response.success) HttpStatusCode.OK else HttpStatusCode.NotFound
            call.respond(status, response)
        }

        post {
            try {
                val dto = call.receive<CourtDTO>()
                val response = service.createCourt(dto)
                val status = if (response.success) HttpStatusCode.Created else HttpStatusCode.BadRequest
                call.respond(status, response)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Neispravan format podataka: ${e.message}")
                )
            }
        }

        put {
            try {
                val dto = call.receive<CourtDTO>()
                val response = service.updateCourt(dto)
                val status = if (response.success) HttpStatusCode.OK else HttpStatusCode.BadRequest
                call.respond(status, response)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Greška pri obradi zahteva")
                )
            }
        }
    }
}