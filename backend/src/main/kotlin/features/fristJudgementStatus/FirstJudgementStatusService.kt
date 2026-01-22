package com.baze3.features.fristJudgementStatus

import com.baze3.common.db.DbResult
import com.baze3.models.ApiResponse
import com.baze3.models.FirstJudgementStatusDTO

class FirstJudgementStatusService(private val repository: FirstJudgementStatusRepository) {

    suspend fun getAllStatuses(): ApiResponse<List<FirstJudgementStatusDTO>> {
        return when (val result = repository.getAll()) {
            is DbResult.Success -> {
                ApiResponse(
                    success = true,
                    data = result.data.map { it.toDTO() },
                    message = "Uspešno dobavljeni statusi"
                )
            }
            is DbResult.Error -> {
                ApiResponse(
                    success = false,
                    data = emptyList(),
                    message = "Greška u bazi: ${result.message}"
                )
            }
        }
    }
}