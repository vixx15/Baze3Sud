package service

import com.baze3.models.ApiResponse
import com.baze3.models.JudgementStatusChangeDTO
import com.baze3.models.JudgementStatusChangeDetailsDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class JudgementStatusChangeService(private val client: HttpClient = httpClient) {

    private val baseUrl = "judgement-status-changes"

    suspend fun getAll(): ApiResponse<List<JudgementStatusChangeDTO>> {
        return try {
            client.get(baseUrl).body()
        } catch (e: Exception) {
            ApiResponse(success = false, message = "Greška: ${e.localizedMessage}")
        }
    }

    suspend fun getHistory(judgementId: String): ApiResponse<List<JudgementStatusChangeDetailsDTO>> {
        return try {
            client.get("$baseUrl/history/$judgementId").body()
        } catch (e: Exception) {
            ApiResponse(success = false, message = "Komunikacija neuspešna: ${e.localizedMessage}")
        }
    }

    suspend fun getSingleDetails(pspId: String, statusId: Long): ApiResponse<JudgementStatusChangeDetailsDTO?> {
        return try {
            client.get("$baseUrl/details-single/$pspId/$statusId").body()
        } catch (e: Exception) {
            ApiResponse(success = false, message = "Greška pri dobavljanju detalja: ${e.localizedMessage}")
        }
    }

    suspend fun create(dto: JudgementStatusChangeDTO): ApiResponse<Int> {
        return try {
            client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }.body()
        } catch (e: Exception) {
            ApiResponse(success = false, message = "Slanje nije uspelo: ${e.localizedMessage}")
        }
    }

    suspend fun update(dto: JudgementStatusChangeDTO): ApiResponse<Int> {
        return try {
            client.put(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }.body()
        } catch (e: Exception) {
            ApiResponse(success = false, message = "Ažuriranje nije uspelo: ${e.localizedMessage}")
        }
    }

    suspend fun delete(pspId: String, statusId: Long): ApiResponse<Int> {
        return try {
            client.delete("$baseUrl/$pspId/$statusId").body()
        } catch (e: Exception) {
            ApiResponse(success = false, message = "Brisanje nije uspelo: ${e.localizedMessage}")
        }
    }
}