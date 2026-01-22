package com.baze3.features.municipality

object MunicipalitySql {
    const val TABLE = "OPSTINA"

    private const val COLUMNS = "ID_OPSTINE, NAZIV, ID_NADLEZNOG_SUDA"

    val findAll = "SELECT $COLUMNS FROM $TABLE ORDER BY NAZIV"

    val findById = "SELECT $COLUMNS FROM $TABLE WHERE ID_OPSTINE = ?"

    const val insert = "INSERT INTO $TABLE ($COLUMNS) VALUES (?, ?, ?)"

    const val update = "UPDATE $TABLE SET NAZIV = ?, ID_NADLEZNOG_SUDA = ? WHERE ID_OPSTINE = ?"
}