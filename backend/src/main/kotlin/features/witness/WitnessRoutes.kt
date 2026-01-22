package com.baze3.features.witness


import com.baze3.models.ApiResponse
import com.baze3.models.WitnessDTO
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

fun Route.witnessRoutes(service: WitnessService) {
    route("/witnesses") {

        get {
            call.respond(service.getAllWitnesses())
        }

        get("/{jmbg}") {
            val jmbg = call.parameters["jmbg"]

            if (jmbg.isNullOrBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "JMBG je obavezan parametar")
                )
                return@get
            }

            val response = service.getWitnessByJmbg(jmbg)

            if (response.success) {
                call.respond(response)
            } else {
                call.respond(HttpStatusCode.NotFound, response)
            }
        }


        post {
            try {
                val dto = call.receive<WitnessDTO>()
                val response = service.createWitness(dto)
                val status = if (response.success) HttpStatusCode.Created else HttpStatusCode.Conflict
                call.respond(status, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse<Unit>(false, "Neispravan format podataka"))
            }
        }

        put {
            try {
                val dto = call.receive<WitnessDTO>()
                val response = service.updateWitness(dto)
                val status = if (response.success) HttpStatusCode.OK else HttpStatusCode.NotFound
                call.respond(status, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse<Unit>(false, "Greška pri obradi podataka"))
            }
        }

        delete("/{jmbg}") {
            val jmbg = call.parameters["jmbg"]
            if (jmbg.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse<Unit>(false, "JMBG je obavezan za brisanje"))
                return@delete
            }
            val response = service.deleteWitness(jmbg)
            val status = if (response.success) HttpStatusCode.OK else HttpStatusCode.InternalServerError
            call.respond(status, response)
        }
    }
}