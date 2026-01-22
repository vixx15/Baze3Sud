package com.baze3.testimony

import com.baze3.common.db.Repository
import com.baze3.features.hering.HearingEntity
import com.baze3.features.witness.WitnessEntity
import com.baze3.common.db.DbResult
import java.sql.ResultSet
import javax.sql.DataSource

class TestimonyRepository(ds: DataSource) : Repository(ds) {

    private fun mapSummary(rs: ResultSet): TestimonyEntity = TestimonyEntity(
        hearingId = rs.getLong("BROJ_RASPRAVE"),
        witnessId = rs.getString("JMBG_SVEDOKA"),
        witnessName = rs.getString("IME_SVEDOKA") ?: "",
        content = rs.getString("SADRZAJ")
    )

    private fun mapDetails(rs: ResultSet): TestimonyDetailsEntity {
        val witness = WitnessEntity(
            id = rs.getString("JMBG_SVEDOKA"),
            name = rs.getString("LICNOIME"),
            place = rs.getString("MESTO"),
            address = rs.getString("ADRESA"),
            fathersName = rs.getString("IME_OCA"),
            mothersName = rs.getString("IME_MAJKE")
        )

        val hearing = HearingEntity(
            id = rs.getLong("BROJ_RASPRAVE"),
            time = rs.getTimestamp("DATUM_VREME").time,
            caseId = rs.getString("BROJ_PREDMETA")
        )

        return TestimonyDetailsEntity(
            hearing = hearing,
            witness = witness,
            content = rs.getString("SADRZAJ"),
            witnessName = rs.getString("IME_SVEDOKA")
        )
    }

    fun getAll(): DbResult<List<TestimonyEntity>> =
        executeQuery(TestimonySql.findAll, mapper = ::mapSummary)

    fun getById(hearingId: Long, witnessId: String): DbResult<TestimonyEntity?> =
        executeQueryOne(TestimonySql.findById, hearingId, witnessId, mapper = ::mapSummary)

    fun getAllWithDetails(): DbResult<List<TestimonyDetailsEntity>> =
        executeQuery(TestimonySql.findAllWithDetails, mapper = ::mapDetails)

    fun getByIdWithDetails(hearingId: Long, witnessId: String): DbResult<TestimonyDetailsEntity?> =
        executeQueryOne(TestimonySql.findByIdWithDetails, hearingId, witnessId, mapper = ::mapDetails)

    fun insertTestimony(dto: TestimonyEntity): DbResult<Int> {
        return executeUpdate(
            TestimonySql.insert,
            dto.hearingId,
            dto.witnessId,
            dto.witnessName?.takeIf { it.isNotEmpty() },
            dto.content?.takeIf { it.isNotEmpty() }
        )
    }

    fun updateTestimony(dto: TestimonyEntity): DbResult<Int> {
        return executeUpdate(
            TestimonySql.update,
            dto.witnessName?.takeIf { it.isNotEmpty() },
            dto.content?.takeIf { it.isNotEmpty() },
            dto.hearingId,
            dto.witnessId,
        )
    }

    fun deleteTestimony(hearingId: Long, witnessId: String): DbResult<Int> {
        return executeUpdate(
            TestimonySql.delete,
            hearingId,
            witnessId
        )
    }
}