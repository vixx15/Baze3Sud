package com.baze3.features.hering

import com.baze3.models.HearingDTO

data class HearingEntity(
    val id: Long,
    val time: Long?,
    val caseId: String?,
){
    fun toDTO(): HearingDTO{
        return HearingDTO(
            id = id,
            time = time,
            caseId = caseId,
        )
    }
}