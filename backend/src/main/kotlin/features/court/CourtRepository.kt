package com.baze3.features.court

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import java.sql.ResultSet
import javax.sql.DataSource

class CourtRepository(ds: DataSource) : Repository(ds) {

    fun mapRow(rs: ResultSet): CourtEntity = CourtEntity(
        id = rs.getLong("ID_SUDA"),
        name = rs.getString("NAZIV"),
        municipalityId = rs.getLong("ID_SEDISTA")
    )

    fun getAll(): DbResult<List<CourtEntity>> =
        executeQuery(CourtSql.findAll, mapper = ::mapRow)

    fun getById(id: Long): DbResult<CourtEntity?> =
        executeQueryOne(CourtSql.findById, id, mapper = ::mapRow)

    fun insert(court: CourtEntity): DbResult<Int> =
        executeUpdate(CourtSql.insert, court.id, court.name, court.municipalityId)

    fun update(court: CourtEntity): DbResult<Int> =
        executeUpdate(CourtSql.update, court.name, court.municipalityId, court.id)
}