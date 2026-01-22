package com.baze3.features.courtCase

import com.baze3.models.CourtCaseDetailsDTO
import com.baze3.models.CourtDTO
import com.baze3.models.DisputeTypeDTO
import com.baze3.models.JudgeDTO

data class CourtCaseEntity(
    val id: String?,
    val value: Double?,
    val typeId: Long?,
    val typeName: String = "",
    val judgeId: Long?,
    val judgeName: String = "",
    val courtId: Long?,
    val courtName: String = ""
) {
    fun toDetailsDto() = CourtCaseDetailsDTO(
        id = id,
        value = value,
        type =
            DisputeTypeDTO(
                id = typeId,
                name = typeName
            ),
        judge = JudgeDTO(judgeId, judgeName, courtId),
        court = CourtDTO(courtId, courtName, 0)
    )
}