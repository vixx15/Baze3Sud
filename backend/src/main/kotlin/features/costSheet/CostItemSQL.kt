package com.baze3.features.costSheet

object CostItemSQL {
    val findByParentId = """
        SELECT ID_TROSKOVNIKA, RB, VRSTA, BROJ_JEDINICA, NAMENA, CENA_JEDINICE 
        FROM TROSAK 
        WHERE ID_TROSKOVNIKA = ?
    """.trimIndent()

    val insert = """
        INSERT INTO TROSAK (ID_TROSKOVNIKA, RB, VRSTA, BROJ_JEDINICA, NAMENA, CENA_JEDINICE) 
        VALUES (?, ?, ?, ?, ?, ?)
    """.trimIndent()

    val update = """
        UPDATE TROSAK 
        SET VRSTA = ?, BROJ_JEDINICA = ?, NAMENA = ?, CENA_JEDINICE = ? 
        WHERE ID_TROSKOVNIKA = ? AND RB = ?
    """.trimIndent()

    val delete = "DELETE FROM TROSAK WHERE ID_TROSKOVNIKA = ? AND RB = ?"

    val deleteAllForParent = "DELETE FROM TROSAK WHERE ID_TROSKOVNIKA = ?"
}