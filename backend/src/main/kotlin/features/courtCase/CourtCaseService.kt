package com.baze3.features.courtCase

import com.baze3.common.db.DbResult
import com.baze3.models.ApiResponse
import com.baze3.models.CourtCaseDTO
import com.baze3.models.CourtCaseDetailsDTO

class CourtCaseService(private val repository: CourtCaseRepository) {

    fun getAllCases(): ApiResponse<List<CourtCaseDTO>> {
        return when (val result = repository.getAll()) {
            is DbResult.Success -> {
                val dtos = result.data.map { entity ->
                    CourtCaseDTO(
                        id = entity.id,
                        value = entity.value,
                        typeId = entity.typeId,
                        judgeId = entity.judgeId,
                        courtId = entity.courtId
                    )
                }
                ApiResponse(true, "Predmeti uspešno učitani.", dtos)
            }

            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun getCaseDetails(brojPredmeta: String): ApiResponse<CourtCaseDetailsDTO> {
        return when (val result = repository.getDetailsById(brojPredmeta)) {
            is DbResult.Success -> {
                val entity = result.data
                if (entity != null) {
                    ApiResponse(true, "Detalji predmeta učitani.", entity.toDetailsDto())
                } else {
                    ApiResponse(false, "Predmet sa brojem $brojPredmeta nije pronađen.")
                }
            }

            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun createCase(dto: CourtCaseDTO): ApiResponse<Unit> {
        val entity = CourtCaseEntity(
            id = dto.id,
            value = dto.value,
            typeId = dto.typeId,
            judgeId = dto.judgeId,
            courtId = dto.courtId
        )
        return when (val result = repository.insert(entity)) {
            is DbResult.Success -> ApiResponse(true, "Predmet ${dto.id} je uspešno kreiran.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun updateCase(dto: CourtCaseDTO): ApiResponse<Unit> {
        val entity = CourtCaseEntity(
            id = dto.id,
            value = dto.value,
            typeId = dto.typeId,
            judgeId = dto.judgeId,
            courtId = dto.courtId
        )
        return when (val result = repository.update(entity)) {
            is DbResult.Success -> ApiResponse(true, "Izmene na predmetu ${dto.id} su sačuvane.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun deleteCase(brojPredmeta: String): ApiResponse<Unit> {
        return when (val result = repository.delete(brojPredmeta)) {
            is DbResult.Success -> ApiResponse(true, "Predmet $brojPredmeta je obrisan.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }
}