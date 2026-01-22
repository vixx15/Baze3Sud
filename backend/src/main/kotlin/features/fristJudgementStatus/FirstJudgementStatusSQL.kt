package com.baze3.features.fristJudgementStatus

object FirstJudgementStatusSQL {
    const val TABLE = "STATUS_PSP"

    private const val COLUMNS = "ID_STATUSA, NAZIV_STATUSA"

    val findAll = "SELECT $COLUMNS FROM $TABLE"

    val findById = "SELECT $COLUMNS FROM $TABLE WHERE ID_STATUSA = ?"

    const val insert = "INSERT INTO $TABLE ($COLUMNS) VALUES (?, ?)"

    const val update = "UPDATE $TABLE SET NAZIV_STATUSA = ? WHERE ID_STATUSA = ?"

    const val delete = "DELETE FROM $TABLE WHERE ID_STATUSA = ?"

}