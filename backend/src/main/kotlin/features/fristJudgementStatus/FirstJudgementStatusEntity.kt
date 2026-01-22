package com.baze3.features.fristJudgementStatus

import com.baze3.models.FirstJudgementStatusDTO

data class FirstJudgementStatusEntity(
    val id: Long,
    val name: String
) {
    fun toDTO(): FirstJudgementStatusDTO {
        return FirstJudgementStatusDTO(
            id = id,
            name = name,
        )
    }
}
