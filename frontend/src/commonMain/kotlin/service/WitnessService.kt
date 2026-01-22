package service

import com.baze3.models.ApiResponse
import com.baze3.models.WitnessDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class WitnessService(private val client: HttpClient = httpClient) {

    suspend fun fetchAllWitnesses(): ApiResponse<List<WitnessDTO>> {
        return try {
            client.get("/witnesses").body()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = "Problem sa konekcijom: ${e.message}"
            )
        }
    }

    suspend fun fetchWitnessByJmbg(jmbg: String): ApiResponse<WitnessDTO> {
        return try {
            client.get("witnesses/$jmbg").body()
        } catch (e: Exception) {
            ApiResponse(
                success = false,
                message = "Greška pri pronalaženju svedoka: ${e.message}"
            )
        }
    }

    suspend fun createWitness(dto: WitnessDTO): ApiResponse<Unit> =
        client.post("witnesses") { setBody(dto); contentType(ContentType.Application.Json) }.body()

    suspend fun updateWitness(dto: WitnessDTO): ApiResponse<Unit> =
        client.put("witnesses") { setBody(dto); contentType(ContentType.Application.Json) }.body()

    suspend fun deleteWitness(jmbg: String): ApiResponse<Unit> =
        client.delete("witnesses/$jmbg").body()
}
