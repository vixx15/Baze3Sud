package com.baze3.features.witness

object WitnessSql {
    const val TABLE = "SVEDOK"

    private const val COLUMNS = "JMBG, LICNOIME, MESTO, ADRESA, IME_OCA, IME_MAJKE"

    val findAll = "SELECT $COLUMNS FROM $TABLE ORDER BY LICNOIME"

    val findById = "SELECT $COLUMNS FROM $TABLE WHERE JMBG = ?"

    const val insert = "INSERT INTO $TABLE ($COLUMNS) VALUES (?, ?, ?, ?, ?, ?)"

    const val update = """
        UPDATE $TABLE 
        SET LICNOIME = ?, MESTO = ?, ADRESA = ?, IME_OCA = ?, IME_MAJKE = ? 
        WHERE JMBG = ?
    """

    const val delete = "DELETE FROM $TABLE WHERE JMBG = ?"
}