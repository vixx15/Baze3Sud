package com.baze3.features.testimony

import com.baze3.models.ApiResponse
import com.baze3.models.TestimonyDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.testimonyRoutes(service: TestimonyService) {
    route("/testimonies") {

        get {
            call.respond(service.getAllTestimonies())
        }

        get("/testimony") {
            val hearingId = call.request.queryParameters["hearingId"]?.toLongOrNull()
            val witnessId = call.request.queryParameters["witnessId"]

            if (hearingId != null && witnessId != null) {
                call.respond(service.getTestimonyDetails(hearingId, witnessId))
            } else {
                call.respond(HttpStatusCode.BadRequest, ApiResponse<Unit>(false, "Nedostaju parametri"))
            }
        }

        post {
            try {
                val testimonyDTO = call.receive<TestimonyDTO>()
                val result = service.insertTestimony(testimonyDTO)
                call.respond(result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse<Unit>(false, "Greška: ${e.message}"))
            }
        }

        put {
            try {
                val dto = call.receive<TestimonyDTO>()
                val result = service.updateTestimony(dto)
                call.respond(result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse<Unit>(false, e.message ?: "Greška"))
            }
        }

        delete {
            val hId = call.request.queryParameters["hearingId"]?.toLongOrNull()
            val wId = call.request.queryParameters["witnessId"]

            if (hId == null || wId == null) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse<Unit>(false, "Nedostaju ID-jevi"))
                return@delete
            }

            val result = service.deleteTestimony(hId, wId)
            call.respond(result)
        }
    }
}