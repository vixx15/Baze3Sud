package com.baze3.features.hering

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import java.sql.ResultSet
import javax.sql.DataSource

class HearingRepository(ds: DataSource) : Repository(ds) {

    private fun mapRow(rs: ResultSet): HearingEntity = HearingEntity(
        id = rs.getLong("BROJ_RASPRAVE"),
        time = rs.getTimestamp("DATUM_VREME").time,
        caseId = rs.getString("BROJ_PREDMETA")
    )

    fun getAll(): DbResult<List<HearingEntity>> =
        executeQuery(HearingSql.findAll, mapper = ::mapRow)

    fun getById(id: Long): DbResult<HearingEntity?> =
        executeQueryOne(HearingSql.findById, id, mapper = ::mapRow)
}