package com.baze3.testimony

object TestimonySql {
    const val TABLE = "SVEDOCENJE SV"

    private const val COLUMNS = "SV.BROJ_RASPRAVE, SV.JMBG_SVEDOKA, SV.SADRZAJ, SV.IME_SVEDOKA"

    private const val JOIN_COLUMNS = """
        SV.BROJ_RASPRAVE, SV.JMBG_SVEDOKA, SV.SADRZAJ, SV.IME_SVEDOKA,
        R.DATUM_VREME, R.BROJ_PREDMETA,
        S.LICNOIME, S.MESTO, S.ADRESA, S.IME_OCA, S.IME_MAJKE
    """

    private const val JOINS = """
        JOIN RASPRAVA R ON SV.BROJ_RASPRAVE = R.BROJ_RASPRAVE
        JOIN SVEDOK S ON SV.JMBG_SVEDOKA = S.JMBG
    """

    val findAllWithDetails = """
        SELECT $JOIN_COLUMNS 
        FROM $TABLE 
        $JOINS
    """.trimIndent()

    val findByIdWithDetails = """
        SELECT $JOIN_COLUMNS 
        FROM $TABLE 
        $JOINS 
        WHERE SV.BROJ_RASPRAVE = ? AND SV.JMBG_SVEDOKA = ?
    """.trimIndent()

    val findAll = "SELECT $COLUMNS FROM $TABLE"

    // Composite Primary Key lookup
    val findById = "SELECT $COLUMNS FROM $TABLE WHERE SV.BROJ_RASPRAVE = ? AND SV.JMBG_SVEDOKA = ?"

    const val insert = "INSERT INTO $TABLE (BROJ_RASPRAVE, JMBG_SVEDOKA, IME_SVEDOKA, SADRZAJ) VALUES (?, ?, ?, ?)"

    const val update = """
    UPDATE $TABLE 
    SET IME_SVEDOKA = ?, 
        SADRZAJ = ? 
    WHERE BROJ_RASPRAVE = ? AND JMBG_SVEDOKA = ?
"""

    const val delete = "DELETE FROM $TABLE WHERE BROJ_RASPRAVE = ? AND JMBG_SVEDOKA = ?"
}