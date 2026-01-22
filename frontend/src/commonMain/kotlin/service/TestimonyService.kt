package service

import com.baze3.models.ApiResponse
import com.baze3.models.TestimonyDTO
import com.baze3.models.TestimonyDetailsDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class TestimonyRemoteService(private val client: HttpClient = httpClient) {

    suspend fun getTestimonySummaries(): ApiResponse<List<TestimonyDTO>> {
        return try {
            client.get("testimonies").body<ApiResponse<List<TestimonyDTO>>>()
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(success = false, message = "Greška u komunikaciji: ${e.message}")
        }
    }

    suspend fun getTestimonyDetail(hId: Long, wId: String): ApiResponse<TestimonyDetailsDTO> {
        return try {
            client.get("testimonies/testimony") {
                parameter("hearingId", hId)
                parameter("witnessId", wId)
            }.body<ApiResponse<TestimonyDetailsDTO>>()
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(success = false, message = "Greška pri učitavanju: ${e.message}")
        }
    }

    suspend fun createTestimony(testimonyDTO: TestimonyDTO): ApiResponse<Unit> {
        return try {
            client.post("testimonies") {
                contentType(ContentType.Application.Json)
                setBody(testimonyDTO)
            }.body<ApiResponse<Unit>>()
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(success = false, message = "Slanje nije uspelo: ${e.message}")
        }
    }

    suspend fun updateTestimony(testimonyDTO: TestimonyDTO): ApiResponse<Unit> {
        return try {
            client.put("testimonies") {
                contentType(ContentType.Application.Json)
                setBody(testimonyDTO)
            }.body<ApiResponse<Unit>>()
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(success = false, message = "Ažuriranje nije uspelo: ${e.message}")
        }
    }

    suspend fun deleteTestimony(hearingId: Long, witnessId: String): ApiResponse<Unit> {
        return try {
            client.delete("testimonies") {
                parameter("hearingId", hearingId)
                parameter("witnessId", witnessId)
            }.body<ApiResponse<Unit>>()
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(success = false, message = "Brisanje nije uspelo: ${e.message}")
        }
    }
}