package com.baze3.models

import kotlinx.serialization.Serializable

@Serializable
data class FirstJudgementStatusDTO(
    val id: Long,
    val name: String
)

@Serializable
data class DisputeTypeDTO(
    val id: Long?,
    val name: String?
)

@Serializable
data class VrstaPSPDTO(
    val id: Long,
    val name: String
)

@Serializable
data class CostItemDTO(
    val costSheetId: Long? = null,
    val ordinalId: String? = null,
    val type: String? = null,
    val quantity: Double? = null,
    val purpose: String? = null,
    val unitPrice: Double? = null,
)

@Serializable
data class CostSheetDetailsDTO(
    val id: Long? = null,
    val date: Long? = null,
    val totalAmount: Double? = null,
    val caseNumber: String? = null,
    val personalId: String? = null,
    val items: List<CostItemDTO> = emptyList()
)

@Serializable
data class CostSheetDTO(
    val id: Long? = null,
    val date: Long? = null,
    val totalAmount: Double? = null,
    val caseNumber: String? = null,
    val personalId: String? = null
)

@Serializable
data class WitnessDTO(
    val id: String? = null,
    val name: String? = null,
    val place: String? = null,
    val address: String? = null,
    val fathersName: String? = null,
    val mothersName: String? = null,
)

@Serializable
data class TestimonyDetailsDTO(
    val hearing: HearingDTO? = null,
    val witness: WitnessDTO? = null,
    val content: String? = null,
    val witnessName: String? = null,
)

@Serializable
data class TestimonyDTO(
    val hearingId: Long?,
    val witnessId: String?,
    val content: String?,
    val witnessName: String?
)

@Serializable
data class JudgeDTO(
    val id: Long? = null,
    val name: String? = null,
    val courtId: Long? = null,
)

@Serializable
data class JudgeDetailsDTO(
    val id: Long? = null,
    val name: String? = null,
    val court: CourtDTO? = null
)

@Serializable
data class CourtDTO(
    val id: Long? = null,
    val name: String? = null,
    val municipalityId: Long? = null,
)

@Serializable
data class PartyDTO(
    val id: String,
    val name: String,
    val place: String,
    val address: String,
    val representativeId: String? = null,
)

@Serializable
data class HearingDTO(
    val id: Long,
    val time: Long? = null,
    val caseId: String? = null,
)

@Serializable
data class LegalRepresentative(
    val id: String,
    val name: String,
    val place: String,
    val address: String,
    val occupation: String,
)

@Serializable
data class JudgementDTO(
    val id: String? = null,
    val date: Long? = null,
    val remedy: String? = null,
    val hearingId: Long? = null,
    val caseID: String? = null,
    val typeId: Long? = null,
    val reasoning: String? = null,
)

@Serializable
data class CourtCaseDTO(
    val id: String?,
    val value: Double?,
    val typeId: Long?,
    val judgeId: Long?,
    val courtId: Long?,
)

@Serializable
data class CourtCaseDetailsDTO(
    val id: String? = null,
    val value: Double? = null,
    val type: DisputeTypeDTO? = null,
    val judge: JudgeDTO? = null,
    val court: CourtDTO? = null,
)

@Serializable
data class MunicipalityDTO(
    val id: Long,
    val name: String,
    val courtId: Long? = null,
)

@Serializable
data class JudgementStatusChangeDTO(
    val judgementId: String? = null,
    val statusId: Long? = null,
    val date: Long? = null,
    val isCurrent: Boolean? = null,
)

@Serializable
data class JudgementStatusChangeDetailsDTO(
    val judgement: JudgementDTO? = null,
    val status: FirstJudgementStatusDTO? = null,
    val date: Long? = null,
    val isCurrent: Boolean? = null
)