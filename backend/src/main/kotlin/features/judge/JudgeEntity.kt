package com.baze3.features.judge

import com.baze3.models.CourtDTO
import com.baze3.models.JudgeDTO
import com.baze3.models.JudgeDetailsDTO

data class JudgeEntity(
    val id: Long? = null,
    val name: String? = null,
    val courtId: Long? = null,
    val courtName: String? = null,
    val courtMunicipalityId: Long? = null,
) {
    fun toDto(): JudgeDTO {
        return JudgeDTO(
            id = id,
            name = name,
            courtId = courtId
        )
    }

    fun toDetailsDto(): JudgeDetailsDTO {
        return JudgeDetailsDTO(
            id = id,
            name = name,
            court = CourtDTO(courtId, courtName, courtMunicipalityId)
        )
    }

}
