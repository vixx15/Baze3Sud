package service

import com.baze3.models.ApiResponse
import com.baze3.models.CourtCaseDTO
import com.baze3.models.CourtCaseDetailsDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CourtCaseService(private val client: HttpClient = httpClient) {

    suspend fun getAllCases(): ApiResponse<List<CourtCaseDTO>> {
        return try {
            client.get("cases").body()
        } catch (e: Exception) {
            ApiResponse(false, "Greška pri povezivanju: ${e.localizedMessage}")
        }
    }

    suspend fun getCaseDetails(brojPredmeta: String): ApiResponse<CourtCaseDetailsDTO> {
        return try {
            client.get("cases/details") {
                parameter("id", brojPredmeta)
            }.body()
        } catch (e: Exception) {
            ApiResponse(false, "Greška pri dohvatanju detalja: ${e.localizedMessage}")
        }
    }

    suspend fun createCase(dto: CourtCaseDTO): ApiResponse<Unit> {
        return try {
            client.post("cases") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }.body()
        } catch (e: Exception) {
            ApiResponse(false, "Neuspešno kreiranje predmeta: ${e.localizedMessage}")
        }
    }

    suspend fun updateCase(dto: CourtCaseDTO): ApiResponse<Unit> {
        return try {
            client.put("cases") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }.body()
        } catch (e: Exception) {
            ApiResponse(false, "Neuspešna izmena predmeta: ${e.localizedMessage}")
        }
    }

    suspend fun deleteCase(id: String): ApiResponse<Unit> {
        return try {
            client.delete("cases/$id").body()
        } catch (e: Exception) {
            ApiResponse(false, "Greška pri brisanju: ${e.localizedMessage}")
        }
    }
}