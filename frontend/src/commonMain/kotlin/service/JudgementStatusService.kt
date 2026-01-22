package service

import com.baze3.models.ApiResponse
import com.baze3.models.FirstJudgementStatusDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class JudgementStatusService(private val client: HttpClient = httpClient) {

    suspend fun getAll(): ApiResponse<List<FirstJudgementStatusDTO>> {
        return try {
            client.get("judgement-statuses").body()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = "Greška u komunikaciji sa serverom: ${e.localizedMessage}",
                data = emptyList()
            )
        }
    }
}