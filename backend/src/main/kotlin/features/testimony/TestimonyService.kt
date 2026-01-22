package com.baze3.features.testimony

import com.baze3.models.ApiResponse
import com.baze3.common.db.DbResult
import com.baze3.models.TestimonyDTO
import com.baze3.models.TestimonyDetailsDTO

class TestimonyService(private val repository: TestimonyRepository) {

    fun getAllTestimonies(): ApiResponse<List<TestimonyDTO>> {
        return when (val result = repository.getAll()) {
            is DbResult.Success -> ApiResponse(true, "Podaci učitani", result.data.map { it.toDTO() })
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun getTestimonyDetails(hearingId: Long, witnessId: String): ApiResponse<TestimonyDetailsDTO> {
        return when (val result = repository.getByIdWithDetails(hearingId, witnessId)) {
            is DbResult.Success -> {
                val dto = result.data?.toDTO()
                if (dto != null) ApiResponse(true, "Detalji učitani", dto)
                else ApiResponse(false, "Svedočenje nije pronađeno")
            }
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun insertTestimony(testimonyDTO: TestimonyDTO): ApiResponse<Unit> {
        val result = repository.insertTestimony(testimonyDTO.toEntity())
        return handleMutationResult(result, "Svedočenje uspešno sačuvano")
    }

    fun updateTestimony(testimonyDTO: TestimonyDTO): ApiResponse<Unit> {
        val result = repository.updateTestimony(testimonyDTO.toEntity())
        return handleMutationResult(result, "Svedočenje uspešno ažurirano")
    }

    fun deleteTestimony(hearingId: Long, witnessId: String): ApiResponse<Unit> {
        val result = repository.deleteTestimony(hearingId, witnessId)
        return handleMutationResult(result, "Svedočenje obrisano")
    }

    private fun handleMutationResult(result: DbResult<Int>, successMsg: String): ApiResponse<Unit> {
        return when (result) {
            is DbResult.Success -> {
                if (result.data > 0) ApiResponse(true, successMsg)
                else ApiResponse(false, "Nijedan red nije izmenjen (proverite ključeve)")
            }
            is DbResult.Error -> {
                ApiResponse(false, result.message)
            }
        }
    }
}

fun TestimonyDTO.toEntity(): TestimonyEntity {
    return TestimonyEntity(
        hearingId = hearingId,
        witnessId = witnessId,
        content = content,
        witnessName = witnessName,
    )
}