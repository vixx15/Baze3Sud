package com.baze3.features.judge

import com.baze3.models.ApiResponse
import com.baze3.models.JudgeDTO
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

fun Route.judgeRoutes(service: JudgeService) {
    route("/judges") {


        get {
            call.respond(service.getAllJudges())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Nevalidan ID sudije")
                )
                return@get
            }

            val response = service.getJudgeDetails(id)
            val status = if (response.success) HttpStatusCode.OK else HttpStatusCode.NotFound
            call.respond(status, response)
        }


        post {
            try {
                val dto = call.receive<JudgeDTO>()
                val response = service.createJudge(dto)
                val status = if (response.success) HttpStatusCode.Created else HttpStatusCode.BadRequest
                call.respond(status, response)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Greška u formatu: ${e.message}")
                )
            }
        }

        put {
            try {
                val dto = call.receive<JudgeDTO>()
                val response = service.updateJudge(dto)
                val status = if (response.success) HttpStatusCode.OK else HttpStatusCode.BadRequest
                call.respond(status, response)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Greška pri obradi: ${e.message}")
                )
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "ID je obavezan za brisanje")
                )
                return@delete
            }
            call.respond(service.deleteJudge(id))
        }
    }
}