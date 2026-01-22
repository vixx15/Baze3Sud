package com.baze3.features.disputeType

import com.baze3.models.DisputeTypeDTO

data class DisputeTypeEntity(
    val id: Long,
    val name: String
) {
    fun toDTO(): DisputeTypeDTO {
        return DisputeTypeDTO(
            id = id,
            name = name,
        )
    }
}

