package com.baze3.features.judgmentStatusChange

import com.baze3.common.db.DbResult
import com.baze3.features.judgement.JudgementEntity
import com.baze3.features.judgement.toDTO
import com.baze3.models.ApiResponse
import com.baze3.models.JudgementDTO
import com.baze3.models.JudgementStatusChangeDTO
import com.baze3.models.JudgementStatusChangeDetailsDTO

class JudgementStatusChangeService(private val repo: JudgementStatusChangeRepository) {

    suspend fun getAllChanges(): ApiResponse<List<JudgementStatusChangeDTO>> {
        return when (val result = repo.getAll()) {
            is DbResult.Success -> ApiResponse(
                success = true,
                data = result.data.map { it.toDTO() },
                message = "Sve izmene statusa su uspešno učitane."
            )

            is DbResult.Error -> ApiResponse(
                success = false,
                message = "Neuspešno učitavanje izmena: ${result.message}"
            )
        }
    }

    suspend fun getHistory(judgementId: String): ApiResponse<List<JudgementStatusChangeDetailsDTO>> {
        return when (val result = repo.getDetailsByJudgementId(judgementId)) {
            is DbResult.Success -> ApiResponse(
                success = true,
                data = result.data.map { it.toDetailsDTO() },
                message = "Istorija statusa za presudu $judgementId je uspešno učitana."
            )

            is DbResult.Error -> ApiResponse(
                success = false,
                message = "Greška pri dobavljanju istorije za $judgementId: ${result.message}"
            )
        }
    }

    suspend fun createChange(dto: JudgementStatusChangeDTO): ApiResponse<Int> {
        return when (val result = repo.create(dto.toEntity())) {
            is DbResult.Success -> ApiResponse(
                success = true,
                data = result.data,
                message = "Novi status je uspešno evidentiran."
            )

            is DbResult.Error -> ApiResponse(
                success = false,
                message = "Sistem nije mogao da sačuva promenu statusa: ${result.message}"
            )
        }
    }

    suspend fun updateChange(dto: JudgementStatusChangeDTO): ApiResponse<Int> {
        return when (val result = repo.update(dto.toEntity())) {
            is DbResult.Success -> ApiResponse(
                success = true,
                data = result.data,
                message = "Podaci o statusu su uspešno ažurirani."
            )

            is DbResult.Error -> ApiResponse(
                success = false,
                message = "Ažuriranje nije uspelo: ${result.message}"
            )
        }
    }

    suspend fun deleteChange(pspId: String, statusId: Long): ApiResponse<Int> {
        return when (val result = repo.delete(pspId, statusId)) {
            is DbResult.Success -> ApiResponse(
                success = true,
                data = result.data,
                message = "Zapis o statusu je trajno obrisan."
            )

            is DbResult.Error -> ApiResponse(
                success = false,
                message = "Brisanje nije dozvoljeno ili nije uspelo: ${result.message}"
            )
        }
    }

    suspend fun getSingleDetails(pspId: String, statusId: Long): ApiResponse<JudgementStatusChangeDetailsDTO?> {
        return when (val result = repo.getSingleDetails(pspId, statusId)) {
            is DbResult.Success -> ApiResponse(
                success = true,
                data = result.data?.toDetailsDTO(),
                message = if (result.data != null) "Detalji pronađeni." else "Traženi zapis ne postoji."
            )

            is DbResult.Error -> ApiResponse(
                success = false,
                message = "Greška pri pretrazi detalja: ${result.message}"
            )
        }
    }
}

fun JudgementStatusChangeEntity.toDTO() = JudgementStatusChangeDTO(
    judgementId = this.judgementId,
    statusId = this.statusId,
    date = this.date,
    isCurrent = this.isCurrent
)

fun JudgementStatusChangeDetailsEntity.toDetailsDTO() = JudgementStatusChangeDetailsDTO(
    date = this.date,
    isCurrent = this.isCurrent,
    judgement = this.judgement?.toDTO(),
    status = this.status?.toDTO()
)


fun JudgementStatusChangeDTO.toEntity(): JudgementStatusChangeEntity {
    return JudgementStatusChangeEntity(
        judgementId = this.judgementId ?: "",
        statusId = this.statusId ?: 0L,
        date = this.date,
        isCurrent = this.isCurrent ?: false
    )
}