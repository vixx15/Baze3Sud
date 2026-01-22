package com.baze3.features.disputeType

import com.baze3.common.db.DbResult
import com.baze3.models.ApiResponse
import com.baze3.models.DisputeTypeDTO

class DisputeTypeService(private val repository: DisputeTypeRepository) {
    fun getAllDisputeTypes(): ApiResponse<List<DisputeTypeDTO>> {
        return when (val result = repository.getAll()) {
            is DbResult.Success -> ApiResponse(
                success = true,
                message = "Vrste spora učitane.",
                data = result.data.map { DisputeTypeDTO(it.id, it.name) }
            )
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun getDisputeTypeById(id: Long): ApiResponse<DisputeTypeDTO> {
        return when (val result = repository.getById(id)) {
            is DbResult.Success -> {
                val entity = result.data
                if (entity != null) {
                    ApiResponse(true, "Vrsta spora pronađena.", DisputeTypeDTO(entity.id, entity.name))
                } else {
                    ApiResponse(false, "Vrsta spora sa ID-om $id ne postoji.")
                }
            }
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }
}