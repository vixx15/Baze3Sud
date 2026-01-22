package com.baze3.features.courtCase

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import java.sql.ResultSet
import javax.sql.DataSource

class CourtCaseRepository(ds: DataSource) : Repository(ds) {
    private fun mapSimpleRow(rs: ResultSet): CourtCaseEntity = CourtCaseEntity(
        id = rs.getString("BROJ_PREDMETA"),
        value = rs.getDouble("VREDNOST_SPORA"),
        typeId = rs.getLong("ID_VRSTE_SPORA"),
        judgeId = rs.getLong("ID_SUDIJE"),
        courtId = rs.getLong("ID_SUDA")
    )

    private fun mapDetailsRow(rs: ResultSet): CourtCaseEntity = CourtCaseEntity(
        id = rs.getString("BROJ_PREDMETA"),
        value = rs.getDouble("VREDNOST_SPORA"),
        typeId = rs.getLong("ID_VRSTE_SPORA"),
        typeName = rs.getString("NAZIV_VRSTE"),
        judgeId = rs.getLong("ID_SUDIJE"),
        judgeName = rs.getString("licno_ime"),
        courtId = rs.getLong("ID_SUDA"),
        courtName = rs.getString("NAZIV")
    )

    fun getAll(): DbResult<List<CourtCaseEntity>> =
        executeQuery(CourtCaseSql.findAll, mapper = ::mapSimpleRow)

    fun getDetailsById(brojPredmeta: String): DbResult<CourtCaseEntity?> =
        executeQueryOne(CourtCaseSql.findByIdDetails, brojPredmeta, mapper = ::mapDetailsRow)

    fun insert(entity: CourtCaseEntity): DbResult<Int> =
        executeUpdate(
            CourtCaseSql.insert,
            entity.id,
            entity.value,
            entity.typeId,
            entity.judgeId,
            entity.courtId
        )

    fun update(entity: CourtCaseEntity): DbResult<Int> =
        executeUpdate(
            CourtCaseSql.update,
            entity.value,
            entity.typeId,
            entity.judgeId,
            entity.courtId,
            entity.id
        )

    fun delete(brojPredmeta: String): DbResult<Int> =
        executeUpdate(CourtCaseSql.delete, brojPredmeta)
}