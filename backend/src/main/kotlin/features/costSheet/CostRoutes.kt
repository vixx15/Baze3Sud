package com.baze3.features.costSheet

import com.baze3.models.CostItemDTO
import com.baze3.models.CostSheetDetailsDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.costRoutes(service: CostService) {
    route("/cost-sheets") {

        get {
            val result = service.getAllSheets()
            call.respond(result)
        }

        get("{id}/details") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val result = service.getSheetById(id)
            call.respond(result)
        }

        post {
            val sheet = call.receive<CostSheetDetailsDTO>()
            val result = service.createSheet(sheet)
            call.respond(result)
        }

        put {
            val sheet = call.receive<CostSheetDetailsDTO>()
            val result = service.updateSheet(sheet)
            call.respond(result)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val result = service.deleteSheet(id)
            call.respond(result)
        }

        route("{sheetId}/items") {

            get {
                val sheetId = call.parameters["sheetId"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                val result = service.getItemsBySheetId(sheetId)
                call.respond(result)
            }

            post {
                val item = call.receive<CostItemDTO>()
                val result = service.createItem(item)
                call.respond(result)
            }

            put {
                val item = call.receive<CostItemDTO>()
                val result = service.updateItem(item)
                call.respond(result)
            }

            delete("{ordinalId}") {
                val sheetId = call.parameters["sheetId"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val ordinalId = call.parameters["ordinalId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val result = service.deleteItem(sheetId, ordinalId)
                call.respond(result)
            }
        }
    }
}