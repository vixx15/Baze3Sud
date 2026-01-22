package com.baze3.features.costSheet

data class CostItemEntity(
    val costSheetId: Long,
    val ordinalId: String,
    val type: String?,
    val quantity: Double?,
    val purpose: String?,
    val unitPrice: Double?,
)