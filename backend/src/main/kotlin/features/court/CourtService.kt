package com.baze3.features.court

import com.baze3.common.db.DbResult
import com.baze3.models.ApiResponse
import com.baze3.models.CourtDTO

class CourtService(private val repository: CourtRepository) {

    fun getAllCourts(): ApiResponse<List<CourtDTO>> {
        return when (val result = repository.getAll()) {
            is DbResult.Success -> ApiResponse(
                success = true,
                message = "Sudovi uspešno učitani.",
                data = result.data.map { it.toDTO() }
            )

            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun getCourtById(id: Long): ApiResponse<CourtDTO> {
        return when (val result = repository.getById(id)) {
            is DbResult.Success -> {
                val dto = result.data?.toDTO()
                if (dto != null) {
                    ApiResponse(true, "Sud pronađen.", dto)
                } else {
                    ApiResponse(false, "Sud sa ID-om $id nije pronađen.")
                }
            }

            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun createCourt(dto: CourtDTO): ApiResponse<Unit> {
        val entity = CourtEntity(
            id = dto.id,
            name = dto.name,
            municipalityId = dto.municipalityId
        )
        return when (val result = repository.insert(entity)) {
            is DbResult.Success -> ApiResponse(true, "Sud '${dto.name}' je uspešno kreiran.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun updateCourt(dto: CourtDTO): ApiResponse<Unit> {
        val entity = CourtEntity(
            id = dto.id,
            name = dto.name,
            municipalityId = dto.municipalityId
        )
        return when (val result = repository.update(entity)) {
            is DbResult.Success -> ApiResponse(true, "Podaci o sudu '${dto.name}' su ažurirani.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }
}

private fun CourtEntity.toDto() = CourtDTO(
    id = this.id,
    name = this.name,
    municipalityId = this.municipalityId
)