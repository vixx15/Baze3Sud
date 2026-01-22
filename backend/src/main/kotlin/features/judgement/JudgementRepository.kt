package com.baze3.features.judgement

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import java.sql.ResultSet
import javax.sql.DataSource

class JudgementRepository(ds: DataSource) : Repository(ds) {

    fun getAll(): DbResult<List<JudgementEntity>> {
        return executeQuery(
            sql = JudgementSQL.findAll,
            mapper = ::mapRow
        )
    }

    private fun mapRow(rs: ResultSet): JudgementEntity {
        return JudgementEntity(
            id = rs.getString("ID_PSP"),
            // Za Oracle DATE -> Long
            date = rs.getTimestamp("DATUM")?.time,
            remedy = rs.getString("POUKA"),
            hearingId = rs.getLong("ID_RASPRAVE").takeIf { !rs.wasNull() },
            caseID = rs.getString("BROJ_PREDMETA"),
            typeId = rs.getLong("ID_VRSTE_PSP").takeIf { !rs.wasNull() },
            reasoning = rs.getString("OBRAZLOZENJE")
        )
    }
}