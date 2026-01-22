package com.baze3.features.witness

import com.baze3.common.db.DbResult
import com.baze3.models.ApiResponse
import com.baze3.models.WitnessDTO

class WitnessService(private val repository: WitnessRepository) {

    fun getAllWitnesses(): ApiResponse<List<WitnessDTO>> {
        return when (val result = repository.getAll()) {
            is DbResult.Success -> {
                ApiResponse(
                    success = true,
                    message = "Svedoci učitani",
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

    fun getWitnessByJmbg(jmbg: String): ApiResponse<WitnessDTO> {
        return when (val result = repository.getByJmbg(jmbg)) {
            is DbResult.Success -> {
                val dto = result.data?.toDTO()
                if (dto != null) {
                    ApiResponse(true, "Svedok pronađen", dto)
                } else {
                    ApiResponse(false, "Svedok sa JMBG-om $jmbg nije pronađen")
                }
            }
            is DbResult.Error -> {
                ApiResponse(false, result.message)
            }
        }
    }

    fun createWitness(dto: WitnessDTO): ApiResponse<Unit> {
        val entity = WitnessEntity(
            id = dto.id,
            name = dto.name,
            place = dto.place,
            address = dto.address,
            fathersName = dto.fathersName,
            mothersName = dto.mothersName
        )

        return when (val result = repository.insert(entity)) {
            is DbResult.Success -> ApiResponse(true, "Svedok ${dto.name} je uspešno sačuvan.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun updateWitness(dto: WitnessDTO): ApiResponse<Unit> {
        val entity = WitnessEntity(
            id = dto.id,
            name = dto.name,
            place = dto.place,
            address = dto.address,
            fathersName = dto.fathersName,
            mothersName = dto.mothersName
        )

        return when (val result = repository.update(entity)) {
            is DbResult.Success -> ApiResponse(true, "Podaci za svedoka ${dto.name} su ažurirani.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }

    fun deleteWitness(jmbg: String): ApiResponse<Unit> {
        return when (val result = repository.delete(jmbg)) {
            is DbResult.Success -> ApiResponse(true, "Svedok je uspešno obrisan iz sistema.")
            is DbResult.Error -> ApiResponse(false, result.message)
        }
    }
}