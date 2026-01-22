package com.baze3.features.costSheet

object CostSheetSQL {
    val findAll = """
        SELECT ID_TROSKOVNIKA, DATUM, UKUPNO, BROJ_PREDMETA, JMBG 
        FROM TROSKOVNIK
    """.trimIndent()

    val findById = """
        SELECT ID_TROSKOVNIKA, DATUM, UKUPNO, BROJ_PREDMETA, JMBG 
        FROM TROSKOVNIK 
        WHERE ID_TROSKOVNIKA = ?
    """.trimIndent()

    val insert = """
        INSERT INTO TROSKOVNIK (ID_TROSKOVNIKA, DATUM, UKUPNO, BROJ_PREDMETA, JMBG) 
        VALUES (?, ?, ?, ?, ?)
    """.trimIndent()

    val update = """
        UPDATE TROSKOVNIK 
        SET DATUM = ?, UKUPNO = ?, BROJ_PREDMETA = ?, JMBG = ? 
        WHERE ID_TROSKOVNIKA = ?
    """.trimIndent()

    val delete = "DELETE FROM TROSKOVNIK WHERE ID_TROSKOVNIKA = ?"

    val findDetailsWithItems = """
    SELECT 
        -- CostSheet (Indeksi 1-5)
        T.ID_TROSKOVNIKA, T.DATUM, T.UKUPNO, T.BROJ_PREDMETA, T.JMBG,
        -- CostItem (Indeksi 6-10 - preskačemo ponovljeni ID_TROSKOVNIKA)
        S.RB, S.VRSTA, S.BROJ_JEDINICA, S.NAMENA, S.CENA_JEDINICE
    FROM TROSKOVNIK T
    LEFT JOIN TROSAK S ON T.ID_TROSKOVNIKA = S.ID_TROSKOVNIKA
    WHERE T.ID_TROSKOVNIKA = ?
""".trimIndent()
}