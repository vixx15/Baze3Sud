package com.baze3.features.judgmentStatusChange

import com.baze3.features.fristJudgementStatus.FirstJudgementStatusEntity
import com.baze3.features.judgement.JudgementEntity

data class JudgementStatusChangeEntity(
    val judgementId: String? = null,
    val statusId: Long? = null,
    val date: Long? = null,
    val isCurrent: Boolean? = null,
)

data class JudgementStatusChangeDetailsEntity(
    val judgement: JudgementEntity? = null,
    val status: FirstJudgementStatusEntity? = null,
    val statusName: String? = null,
    val date: Long? = null,
    val isCurrent: Boolean? = null
)