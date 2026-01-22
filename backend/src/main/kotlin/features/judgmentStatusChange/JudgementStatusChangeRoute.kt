package com.baze3.features.judgmentStatusChange

import com.baze3.models.JudgementStatusChangeDTO
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

fun Route.judgementStatusChangeRoutes(service: JudgementStatusChangeService) {

    route("/judgement-status-changes") {

        get {
            val response = service.getAllChanges()
            call.respond(response)
        }

        get("/history/{judgementId}") {
            val id = call.parameters["judgementId"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Nedostaje ID presude")

            val response = service.getHistory(id)
            call.respond(response)
        }

        get("/details-single/{pspId}/{statusId}") {
            val pspId = call.parameters["pspId"]
            val statusId = call.parameters["statusId"]?.toLongOrNull()

            if (pspId == null || statusId == null) {
                return@get call.respond(HttpStatusCode.BadRequest, "Neispravni parametri ključa")
            }

            val response = service.getSingleDetails(pspId, statusId)
            call.respond(response)
        }

        post {
            val dto = call.receive<JudgementStatusChangeDTO>()
            val response = service.createChange(dto)
            call.respond(response)
        }

        put {
            val dto = call.receive<JudgementStatusChangeDTO>()
            val response = service.updateChange(dto)
            call.respond(response)
        }

        delete("/{pspId}/{statusId}") {
            val pspId = call.parameters["pspId"]
            val statusId = call.parameters["statusId"]?.toLongOrNull()

            if (pspId == null || statusId == null) {
                return@delete call.respond(HttpStatusCode.BadRequest, "Nedostaju ključevi za brisanje")
            }

            val response = service.deleteChange(pspId, statusId)
            call.respond(response)
        }
    }
}