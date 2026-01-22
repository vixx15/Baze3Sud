package com.baze3.features.municipality

import com.baze3.models.MunicipalityDTO

data class MunicipalityEntity(
    val id: Long,
    val name: String,
    val courtId: Long? = null,
){
    fun toDTO(): MunicipalityDTO{
        return MunicipalityDTO(
            id = id,
            name = name,
            courtId = courtId,
        )
    }
}