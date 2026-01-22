package com.baze3.features.courtCase

object CourtCaseSql {
    const val TABLE = "PREDMET"

    val findAll = """
        SELECT BROJ_PREDMETA, VREDNOST_SPORA, ID_VRSTE_SPORA, ID_SUDIJE, ID_SUDA 
        FROM $TABLE 
    """.trimIndent()

    val findByIdDetails = """ SELECT p.BROJ_PREDMETA, p.VREDNOST_SPORA, p.ID_VRSTE_SPORA, vs.NAZIV_VRSTE, s.ID_SUDIJE, s.LICNO_IME, sud.ID_SUDA, sud.NAZIV FROM $TABLE p JOIN SUDIJA s ON p.ID_SUDIJE = s.ID_SUDIJE JOIN SUD sud ON p.ID_SUDA = sud.ID_SUDA JOIN VRSTA_SPORA vs ON p.ID_VRSTE_SPORA = vs.ID_VRSTE_SPORA WHERE p.BROJ_PREDMETA = ? """.trimIndent()

    const val insert = "INSERT INTO $TABLE (BROJ_PREDMETA, VREDNOST_SPORA, ID_VRSTE_SPORA, ID_SUDIJE, ID_SUDA) VALUES (?, ?, ?, ?, ?)"
    const val update = "UPDATE $TABLE SET VREDNOST_SPORA = ?, ID_VRSTE_SPORA = ?, ID_SUDIJE = ?, ID_SUDA = ? WHERE BROJ_PREDMETA = ?"
    const val delete = "DELETE FROM $TABLE WHERE BROJ_PREDMETA = ?"
}