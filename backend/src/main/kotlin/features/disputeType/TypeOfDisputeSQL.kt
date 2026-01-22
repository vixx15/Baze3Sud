package com.baze3.features.disputeType

object TypeOfDisputeSQL {
    const val TABLE = "VRSTA_SPORA"

    private const val COLUMNS = "ID_VRSTE_SPORA, NAZIV_VRSTE"

    val findAll = "SELECT $COLUMNS FROM $TABLE ORDER BY ID_VRSTE_SPORA"

    val findById = "SELECT $COLUMNS FROM $TABLE WHERE ID_VRSTE_SPORA = ?"
}