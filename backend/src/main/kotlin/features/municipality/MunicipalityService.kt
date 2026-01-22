package com.baze3.features.municipality

import com.baze3.models.MunicipalityDTO

class MunicipalityService(private val repository: MunicipalityRepository) {

    fun getAllMunicipalities(): List<MunicipalityDTO> = repository.getAll().map { it.toDTO() }

    fun getMunicipalityById(id: Long): MunicipalityDTO? {
        return repository.getById(id)?.toDTO()
    }
}