package com.baze3.features.hearing

import com.baze3.common.db.DbResult
import com.baze3.features.hering.HearingRepository
import com.baze3.models.ApiResponse
import com.baze3.models.HearingDTO

class HearingService(private val repository: HearingRepository) {

    fun getAllHearings(): ApiResponse<List<HearingDTO>> {
        return when (val result = repository.getAll()) {
            is DbResult.Success -> {
                ApiResponse(
                    success = true,
                    message = "Rasprave uspešno učitane.",
                    data = result.data.map { it.toDTO() }
                )
            }
            is DbResult.Error -> {
                ApiResponse(
                    success = false,
                    message = result.message
                )
            }
        }
    }

    fun getHearingById(id: Long): ApiResponse<HearingDTO> {
        return when (val result = repository.getById(id)) {
            is DbResult.Success -> {
                val dto = result.data?.toDTO()
                if (dto != null) {
                    ApiResponse(true, "Rasprava pronađena.", dto)
                } else {
                    ApiResponse(false, "Rasprava sa ID-jem $id nije pronađena u bazi.")
                }
            }
            is DbResult.Error -> {
                ApiResponse(
                    success = false,
                    message = result.message
                )
            }
        }
    }
}