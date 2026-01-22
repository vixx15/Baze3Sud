package service

import com.baze3.models.ApiResponse
import com.baze3.models.JudgementDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*

class JudgementService(private val client: HttpClient=httpClient) {
    suspend fun getAll(): ApiResponse<List<JudgementDTO>> {
        return try {
            client.get("judgements") .body()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = "Greška u komunikaciji sa serverom: ${e.localizedMessage}",
                data = emptyList()
            )
        }
    }
}