package com.baze3.features.municipality

import com.baze3.common.db.BaseRepository
import java.sql.ResultSet
import javax.sql.DataSource

class MunicipalityRepository(ds: DataSource) : BaseRepository<MunicipalityEntity>(ds) {

    override fun mapRow(rs: ResultSet): MunicipalityEntity = MunicipalityEntity(
        id = rs.getLong("ID_OPSTINE"),
        name = rs.getString("NAZIV"),
        courtId = rs.getLong("ID_NADLEZNOG_SUDA")
    )

    fun getAll(): List<MunicipalityEntity> = queryList(MunicipalitySql.findAll)

    fun getById(id: Long): MunicipalityEntity? = queryOne(MunicipalitySql.findById, id)
}