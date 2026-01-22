package service

import com.baze3.models.ApiResponse
import com.baze3.models.CourtDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class CourtService(private val client: HttpClient = httpClient) {

    suspend fun getAllCourts(): ApiResponse<List<CourtDTO>> {
        return try {
            client.get("courts").body()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = "Greška pri učitavanju sudova: ${e.localizedMessage}"
            )
        }
    }

    suspend fun getCourtById(id: Long): ApiResponse<CourtDTO> {
        return try {
            client.get("courts/$id").body()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = "Greška pri dohvatanju suda: ${e.localizedMessage}"
            )
        }
    }
}