package service

import com.baze3.models.ApiResponse
import com.baze3.models.DisputeTypeDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class DisputeTypeService(private val client: HttpClient = httpClient) {
    suspend fun getAll(): ApiResponse<List<DisputeTypeDTO>> {
        return try {
            client.get("dispute-types").body()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = "Greška pri učitavanju vrsta spora: ${e.localizedMessage}",
                data = emptyList()
            )
        }
    }
}