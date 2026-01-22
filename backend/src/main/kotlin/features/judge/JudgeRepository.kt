package com.baze3.features.judge

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import java.sql.ResultSet
import javax.sql.DataSource

class JudgeRepository(ds: DataSource) : Repository(ds) {
    private fun mapSimpleRow(rs: ResultSet): JudgeEntity = JudgeEntity(
        id = rs.getLong("ID_SUDIJE"),
        name = rs.getString("LICNO_IME"),
        courtId = rs.getLong("ID_SUDA"),
        courtName = "",
        courtMunicipalityId = 0L
    )

    private fun mapDetailsRow(rs: ResultSet): JudgeEntity = JudgeEntity(
        id = rs.getLong("ID_SUDIJE"),
        name = rs.getString("LICNO_IME"),
        courtId = rs.getLong("ID_SUDA"),
        courtName = rs.getString("NAZIV_SUDA"),
        courtMunicipalityId = rs.getLong("ID_SEDISTA")
    )

    fun getAll(): DbResult<List<JudgeEntity>> =
        executeQuery(JudgeSql.findAll, mapper = ::mapSimpleRow)

    fun getDetailsById(id: Long): DbResult<JudgeEntity?> =
        executeQueryOne(JudgeSql.findByIdWithCourt, id, mapper = ::mapDetailsRow)

    fun insert(id: Long?, name: String?, courtId: Long?): DbResult<Int> =
        executeUpdate(JudgeSql.insert, id, name, courtId)

    fun update(judge: JudgeEntity): DbResult<Int> =
        executeUpdate(JudgeSql.update, judge.name, judge.courtId, judge.id)

    fun delete(id: Long): DbResult<Int> =
        executeUpdate(JudgeSql.delete, id)
}