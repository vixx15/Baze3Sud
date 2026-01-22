package com.baze3.features.judgement

object JudgementSQL {
    val findAll = """
        SELECT 
            ID_PSP, 
            DATUM, 
            POUKA, 
            ID_RASPRAVE, 
            BROJ_PREDMETA, 
            ID_VRSTE_PSP, 
            OBRAZLOZENJE 
        FROM PS_PRESUDA
    """.trimIndent()
}