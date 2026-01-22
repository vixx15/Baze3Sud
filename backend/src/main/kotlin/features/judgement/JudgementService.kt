package com.baze3.features.judgement

import com.baze3.common.db.DbResult
import com.baze3.models.ApiResponse
import com.baze3.models.JudgementDTO

class JudgementService(private val repository: JudgementRepository) {
    suspend fun getAllJudgements(): ApiResponse<List<JudgementDTO>> {
        return when (val result = repository.getAll()) {
            is DbResult.Success -> {
                ApiResponse(
                    success = true,
                    data = result.data.map { it.toDTO() },
                    message = "Uspešno dobavljene presude"
                )
            }
            is DbResult.Error -> {
                ApiResponse(
                    success = false,
                    data = emptyList(),
                    message = result.message // Ovde će biti tvoj cleanMessage
                )
            }
        }
    }
}

fun JudgementEntity.toDTO() = JudgementDTO(
    id = id,
    date = date,
    remedy = remedy,
    hearingId = hearingId,
    caseID = caseID,
    typeId = typeId,
    reasoning = reasoning
)