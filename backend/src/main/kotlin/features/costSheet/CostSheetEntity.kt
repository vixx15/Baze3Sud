package com.baze3.features.costSheet

data class CostSheetEntity(
    val id: Long,
    val date: Long?,
    val totalAmount: Double?,
    val caseNumber: String?,
    val personalId: String?
)

