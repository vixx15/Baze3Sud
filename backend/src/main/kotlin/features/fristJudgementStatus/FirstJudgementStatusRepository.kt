package com.baze3.features.fristJudgementStatus

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import java.sql.ResultSet
import javax.sql.DataSource

class FirstJudgementStatusRepository(dataSource: DataSource) :
    Repository(dataSource) {

    fun getAll(): DbResult<List<FirstJudgementStatusEntity>> {
        return executeQuery(FirstJudgementStatusSQL.findAll, mapper = ::mapRow)
    }

    fun mapRow(rs: ResultSet): FirstJudgementStatusEntity {
        return FirstJudgementStatusEntity(
            id = rs.getLong("ID_STATUSA"),
            name = rs.getString("NAZIV_STATUSA")
        )
    }
}