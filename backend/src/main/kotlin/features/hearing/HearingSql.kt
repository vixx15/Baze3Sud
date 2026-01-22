package com.baze3.features.hering

object HearingSql {
    const val TABLE = "RASPRAVA"

    private const val COLUMNS = "BROJ_RASPRAVE, DATUM_VREME, BROJ_PREDMETA"

    val findAll = "SELECT $COLUMNS FROM $TABLE ORDER BY DATUM_VREME DESC"

    val findById = "SELECT $COLUMNS FROM $TABLE WHERE BROJ_RASPRAVE = ?"

    const val insert = "INSERT INTO $TABLE ($COLUMNS) VALUES (?, ?, ?)"

    const val update = "UPDATE $TABLE SET DATUM_VREME = ?, BROJ_PREDMETA = ? WHERE BROJ_RASPRAVE = ?"
}