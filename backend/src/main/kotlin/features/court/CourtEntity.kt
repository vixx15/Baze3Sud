package com.baze3.features.court

import com.baze3.models.CourtDTO

data class CourtEntity(
    val id: Long? = null,
    val name: String? = null,
    val municipalityId: Long? = null,
) {
    fun toDTO(): CourtDTO {
        return CourtDTO(
            id = id,
            name = name,
            municipalityId = municipalityId
        )
    }
}
