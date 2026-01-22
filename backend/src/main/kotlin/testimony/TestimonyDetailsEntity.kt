package com.baze3.testimony

import com.baze3.features.hering.HearingEntity
import com.baze3.features.witness.WitnessEntity
import com.baze3.models.TestimonyDTO
import com.baze3.models.TestimonyDetailsDTO

data class TestimonyEntity(
    val hearingId: Long?,
    val witnessId: String?,
    val content: String?,
    val witnessName: String?
) {
    fun toDTO(): TestimonyDTO {
        return TestimonyDTO(
            hearingId = hearingId,
            witnessId = witnessId,
            content = content,
            witnessName = witnessName
        )
    }
}

data class TestimonyDetailsEntity(
    val hearing: HearingEntity,
    val witness: WitnessEntity,
    val content: String?,
    val witnessName: String?
) {
    fun toDTO(): TestimonyDetailsDTO {
        return TestimonyDetailsDTO(
            hearing = hearing.toDTO(),
            witness = witness.toDTO(),
            content = content,
            witnessName = witnessName
        )
    }
}