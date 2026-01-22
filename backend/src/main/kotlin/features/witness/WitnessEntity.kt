package com.baze3.features.witness

import com.baze3.models.WitnessDTO

data class WitnessEntity(
    val id: String? = null,
    val name: String? = null,
    val place: String? = null,
    val address: String? = null,
    val fathersName: String? = null,
    val mothersName: String? = null,
) {
    fun toDTO(): WitnessDTO {
        return WitnessDTO(
            id = id,
            name = name,
            place = place,
            address = address,
            fathersName = fathersName,
            mothersName = mothersName,
        )
    }
}
