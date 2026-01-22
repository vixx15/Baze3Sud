package service

import com.baze3.models.ApiResponse
import com.baze3.models.HearingDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class HearingService(private val client: HttpClient = httpClient) {
    suspend fun fetchAllHearings(): ApiResponse<List<HearingDTO>> {
        return try {
            client.get("hearings").body()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = "Greška u mrežnoj komunikaciji: ${e.message}"
            )
        }
    }

    suspend fun fetchHearingById(id: Long): ApiResponse<HearingDTO> {
        return try {
            client.get("hearings/$id").body()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = "Greška pri učitavanju rasprave: ${e.message}"
            )
        }
    }
}
