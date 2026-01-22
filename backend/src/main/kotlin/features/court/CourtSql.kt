package com.baze3.features.court

object CourtSql {
    const val TABLE = "SUD"

    private const val COLUMNS = "ID_SUDA, NAZIV, ID_SEDISTA"

    val findAll = "SELECT $COLUMNS FROM $TABLE ORDER BY NAZIV"

    val findById = "SELECT $COLUMNS FROM $TABLE WHERE ID_SUDA = ?"

    const val insert = "INSERT INTO $TABLE ($COLUMNS) VALUES (?, ?, ?)"

    const val update = "UPDATE $TABLE SET NAZIV = ?, ID_SEDISTA = ? WHERE ID_SUDA = ?"
}