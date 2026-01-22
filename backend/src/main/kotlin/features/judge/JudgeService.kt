package com.baze3.features.judge

import com.baze3.common.db.DbResult
import com.baze3.models.ApiResponse
import com.baze3.models.JudgeDTO
import com.baze3.models.JudgeDetailsDTO

class JudgeService(private val repository: JudgeRepository) {

    fun getAllJudges(): ApiResponse<List<JudgeDTO>> {
        return when (val result = repository.getAll()) {
            is DbResult.Success -> ApiResponse(
                success = true,
                message = "Sudije učitane.",
                data = result.data.map {
                    // Ručno mapiramo u osnovni DTO (Summary)
                    JudgeDTO(id = it.id, name = it.name, courtId = it.courtId)
                }
            )

            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }


    fun getJudgeDetails(id: Long): ApiResponse<JudgeDetailsDTO> {
        return when (val result = repository.getDetailsById(id)) {
            is DbResult.Success -> {
                val entity = result.data
                if (entity != null) {
                    ApiResponse(true, "Sudija pronađen.", entity.toDetailsDto())
                } else {
                    ApiResponse(false, "Sudija sa ID $id ne postoji.")
                }
            }

            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }


    fun createJudge(dto: JudgeDTO): ApiResponse<Unit> {
        return when (val result = repository.insert(dto.id, dto.name, dto.courtId)) {
            is DbResult.Success -> ApiResponse(true, "Sudija ${dto.name} uspešno kreiran.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }


    fun updateJudge(dto: JudgeDTO): ApiResponse<Unit> {
        return when (val result = repository.update(JudgeEntity(dto.id, dto.name, dto.courtId))) {
            is DbResult.Success -> ApiResponse(true, "Podaci o sudiji ${dto.name} su ažurirani.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun deleteJudge(id: Long): ApiResponse<Unit> {
        return when (val result = repository.delete(id)) {
            is DbResult.Success -> ApiResponse(true, "Sudija uspešno obrisan.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }
}