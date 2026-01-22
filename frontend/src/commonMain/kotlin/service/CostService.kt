package service

import com.baze3.models.ApiResponse
import com.baze3.models.CostItemDTO
import com.baze3.models.CostSheetDTO
import com.baze3.models.CostSheetDetailsDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class CostService(
    private val client: HttpClient=httpClient,
    private val baseUrl: String = "http://localhost:9090/cost-sheets"
) {

    suspend fun getAllSheets(): ApiResponse<List<CostSheetDTO>> {
        return client.get(baseUrl).body()
    }

    suspend fun getFullDetails(id: Long): ApiResponse<CostSheetDetailsDTO> {
        return client.get("$baseUrl/$id/details").body()
    }

    suspend fun createSheet(sheet: CostSheetDetailsDTO): ApiResponse<Boolean> {
        return client.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(sheet)
        }.body()
    }

    suspend fun updateFullSheet(sheetDetails: CostSheetDetailsDTO): ApiResponse<Boolean> {
        return client.put(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(sheetDetails)
        }.body()
    }

    suspend fun deleteSheet(id: Long): ApiResponse<Int> {
        return client.delete("$baseUrl/$id").body()
    }

    suspend fun getItemsForSheet(sheetId: Long): ApiResponse<List<CostItemDTO>> {
        return client.get("$baseUrl/$sheetId/items").body()
    }

    suspend fun addItem(item: CostItemDTO): ApiResponse<Int> {
        return client.post("$baseUrl/${item.costSheetId}/items") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }.body()
    }

    suspend fun deleteItem(sheetId: Long, ordinalId: String): ApiResponse<Int> {
        return client.delete("$baseUrl/$sheetId/items/$ordinalId").body()
    }
}