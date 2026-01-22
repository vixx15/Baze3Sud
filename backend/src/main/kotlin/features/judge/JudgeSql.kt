package com.baze3.features.judge

object JudgeSql {
    const val TABLE = "SUDIJA"
    const val COURT_TABLE = "SUD"

    private const val SIMPLE_COLUMNS = "ID_SUDIJE, LICNO_IME, ID_SUDA"

    private const val DETAIL_COLUMNS = "s.ID_SUDIJE, s.LICNO_IME, s.ID_SUDA, c.NAZIV AS NAZIV_SUDA, c.ID_SEDISTA"

    val findAll = "SELECT $SIMPLE_COLUMNS FROM $TABLE ORDER BY LICNO_IME"

    val findByIdWithCourt = """
        SELECT $DETAIL_COLUMNS 
        FROM $TABLE s
        JOIN $COURT_TABLE c ON s.ID_SUDA = c.ID_SUDA
        WHERE s.ID_SUDIJE = ?
    """.trimIndent()

    const val insert = "INSERT INTO $TABLE (ID_SUDIJE, LICNO_IME, ID_SUDA) VALUES (?, ?, ?)"
    const val update = "UPDATE $TABLE SET LICNO_IME = ?, ID_SUDA = ? WHERE ID_SUDIJE = ?"
    const val delete = "DELETE FROM $TABLE WHERE ID_SUDIJE = ?"
}