package com.baze3.features.judgmentStatusChange

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import com.baze3.features.fristJudgementStatus.FirstJudgementStatusEntity
import com.baze3.features.judgement.JudgementEntity
import java.sql.ResultSet
import java.sql.Timestamp
import javax.sql.DataSource

class JudgementStatusChangeRepository(ds: DataSource) : Repository(ds) {

    fun getAll(): DbResult<List<JudgementStatusChangeEntity>> =
        executeQuery(JudgementStatusChangeSQL.findAll, mapper = ::mapRow)

    fun getDetailsByJudgementId(id: String): DbResult<List<JudgementStatusChangeDetailsEntity>> =
        executeQuery(JudgementStatusChangeSQL.findDetailsByPspId, id, mapper = ::mapDetailsRow)

    fun getSingleDetails(pspId: String, statusId: Long): DbResult<JudgementStatusChangeDetailsEntity?> {
        return executeQueryOne(
            JudgementStatusChangeSQL.findByCompositeKey,
            pspId,
            statusId,
            mapper = ::mapDetailsRow
        )
    }

    fun create(entity: JudgementStatusChangeEntity): DbResult<Int> =
        executeUpdate(
            JudgementStatusChangeSQL.insert,
            entity.judgementId,
            entity.statusId,
            entity.date?.let { Timestamp(it) },
            if (entity.isCurrent == true) {
                1
            } else if (entity.isCurrent == false) {
                0
            } else {
                null
            }
        )

    fun update(entity: JudgementStatusChangeEntity): DbResult<Int> =
        executeUpdate(
            JudgementStatusChangeSQL.update, entity.date?.let { Timestamp(it) }, if (entity.isCurrent == true) {
                1
            } else if (entity.isCurrent == false) {
                0
            } else {
                null
            }, entity.judgementId, entity.statusId
        )

    fun delete(pspId: String, statusId: Long): DbResult<Int> =
        executeUpdate(JudgementStatusChangeSQL.delete, pspId, statusId)

    private fun mapRow(rs: ResultSet) = JudgementStatusChangeEntity(
        judgementId = rs.getString("ID_PSP"),
        statusId = rs.getLong("ID_STATUSA_PSP"),
        date = rs.getTimestamp("DATUM")?.time,
        isCurrent = rs.getInt("JE_TRENUTNI") == 1
    )

    private fun mapDetailsRow(rs: ResultSet): JudgementStatusChangeDetailsEntity {
        return JudgementStatusChangeDetailsEntity(
            date = rs.getTimestamp(3)?.time,
            isCurrent = rs.getInt("JE_TRENUTNI") == 1,

            status = FirstJudgementStatusEntity(
                id = rs.getLong("ID_STATUSA_PSP"),
                name = rs.getString("NAZIV_STATUSA")
            ),

            judgement = JudgementEntity(
                id = rs.getString("ID_PSP"),
                caseID = rs.getString("BROJ_PREDMETA"),
                date = rs.getTimestamp(7)?.time
            )
        )
    }
}