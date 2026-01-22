package com.baze3.features.disputeType

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import java.sql.ResultSet
import javax.sql.DataSource

class DisputeTypeRepository(ds: DataSource) : Repository(ds) {

    private fun mapRow(rs: ResultSet): DisputeTypeEntity = DisputeTypeEntity(
        id = rs.getLong("ID_VRSTE_SPORA"),
        name = rs.getString("NAZIV_VRSTE")
    )

    fun getAll(): DbResult<List<DisputeTypeEntity>> =
        executeQuery(TypeOfDisputeSQL.findAll, mapper = ::mapRow)

    fun getById(id: Long): DbResult<DisputeTypeEntity?> =
        executeQueryOne(TypeOfDisputeSQL.findById, id, mapper = ::mapRow)
}