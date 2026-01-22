package service

import com.baze3.models.ApiResponse
import com.baze3.models.JudgeDTO
import com.baze3.models.JudgeDetailsDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class JudgeService(private val client: HttpClient = httpClient) {

    suspend fun getAllJudges(): ApiResponse<List<JudgeDTO>> {
        return try {
            client.get("judges").body()
        } catch (e: Exception) {
            ApiResponse(false, "Greška pri povezivanju: ${e.message}")
        }
    }

    suspend fun getJudgeDetails(id: Long): ApiResponse<JudgeDetailsDTO> {
        return try {
            client.get("judges/$id").body()
        } catch (e: Exception) {
            ApiResponse(false, "Greška pri dohvatanju detalja: ${e.message}")
        }
    }

    suspend fun createJudge(dto: JudgeDTO): ApiResponse<Unit> {
        return try {
            client.post("judges") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }.body()
        } catch (e: Exception) {
            ApiResponse(false, "Neuspešno čuvanje: ${e.message}")
        }
    }

    suspend fun updateJudge(dto: JudgeDTO): ApiResponse<Unit> {
        return try {
            client.put("judges") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }.body()
        } catch (e: Exception) {
            ApiResponse(false, "Neuspešna izmena: ${e.message}")
        }
    }

    suspend fun deleteJudge(id: Long): ApiResponse<Unit> {
        return try {
            client.delete("judges/$id").body()
        } catch (e: Exception) {
            ApiResponse(false, "Greška pri brisanju: ${e.message}")
        }
    }
}