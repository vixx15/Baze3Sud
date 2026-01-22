package com.baze3.features.witness

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import java.sql.ResultSet
import javax.sql.DataSource

class WitnessRepository(ds: DataSource) : Repository(ds) {

    fun mapRow(rs: ResultSet): WitnessEntity = WitnessEntity(
        id = rs.getString("JMBG"),
        name = rs.getString("LICNOIME"),
        place = rs.getString("MESTO"),
        address = rs.getString("ADRESA"),
        fathersName = rs.getString("IME_OCA"),
        mothersName = rs.getString("IME_MAJKE")
    )

    fun getAll(): DbResult<List<WitnessEntity>> =
        executeQuery(WitnessSql.findAll, mapper = ::mapRow)

    fun getByJmbg(jmbg: String): DbResult<WitnessEntity?> =
        executeQueryOne(WitnessSql.findById, jmbg, mapper = ::mapRow)

    fun insert(witness: WitnessEntity): DbResult<Int> =
        executeUpdate(
            WitnessSql.insert,
            witness.id,
            witness.name,
            witness.place,
            witness.address,
            witness.fathersName,
            witness.mothersName
        )

    fun update(witness: WitnessEntity): DbResult<Int> =
        executeUpdate(
            WitnessSql.update,
            witness.name,
            witness.place,
            witness.address,
            witness.fathersName,
            witness.mothersName,
            witness.id
        )

    fun delete(jmbg: String): DbResult<Int> =
        executeUpdate(WitnessSql.delete, jmbg)
}