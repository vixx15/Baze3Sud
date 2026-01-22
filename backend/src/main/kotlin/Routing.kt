package com.baze3

import com.baze3.common.db.DataSourceProvider
import com.baze3.features.costSheet.CostRepository
import com.baze3.features.costSheet.CostService
import com.baze3.features.costSheet.costRoutes
import com.baze3.features.court.CourtRepository
import com.baze3.features.court.CourtService
import com.baze3.features.court.courtRoutes
import com.baze3.features.courtCase.CourtCaseRepository
import com.baze3.features.courtCase.CourtCaseService
import com.baze3.features.courtCase.courtCaseRoutes
import com.baze3.features.disputeType.DisputeTypeRepository
import com.baze3.features.disputeType.DisputeTypeService
import com.baze3.features.disputeType.disputeTypeRoutes
import com.baze3.features.fristJudgementStatus.FirstJudgementStatusRepository
import com.baze3.features.fristJudgementStatus.FirstJudgementStatusService
import com.baze3.features.fristJudgementStatus.statusRoutes
import com.baze3.features.hearing.HearingService
import com.baze3.features.hearing.hearingRoutes
import com.baze3.features.hering.HearingRepository
import com.baze3.features.judge.JudgeRepository
import com.baze3.features.judge.JudgeService
import com.baze3.features.judge.judgeRoutes
import com.baze3.features.judgement.JudgementRepository
import com.baze3.features.judgement.JudgementService
import com.baze3.features.judgement.judgementRoutes
import com.baze3.features.judgmentStatusChange.JudgementStatusChangeRepository
import com.baze3.features.judgmentStatusChange.JudgementStatusChangeService
import com.baze3.features.judgmentStatusChange.judgementStatusChangeRoutes
import com.baze3.features.witness.WitnessRepository
import com.baze3.features.witness.WitnessService
import com.baze3.features.witness.witnessRoutes
import com.baze3.features.testimony.TestimonyRepository
import com.baze3.features.testimony.TestimonyService
import com.baze3.features.testimony.testimonyRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        val dataSource = DataSourceProvider.dataSource
        val firstJudgementStatusRepository = FirstJudgementStatusRepository(dataSource)
        val firstJudgementStatusService = FirstJudgementStatusService(firstJudgementStatusRepository)
        statusRoutes(firstJudgementStatusService)

        val disputeTypeRepository = DisputeTypeRepository(dataSource)
        val service = DisputeTypeService(disputeTypeRepository)
        disputeTypeRoutes(service)

        val witnessRepo = WitnessRepository(dataSource)
        val witnessService = WitnessService(witnessRepo)
        witnessRoutes(witnessService)

        val hearingRepository = HearingRepository(dataSource)
        val hearingService = HearingService(hearingRepository)
        hearingRoutes(hearingService)

        val testimonyRepository = TestimonyRepository(dataSource)
        val testimonyService = TestimonyService(testimonyRepository)
        testimonyRoutes(testimonyService)

        val courtCaseRepository = CourtCaseRepository(dataSource)
        val courtCaseService = CourtCaseService(courtCaseRepository)
        courtCaseRoutes(courtCaseService)

        val judgeRepository = JudgeRepository(dataSource)
        val judgeService = JudgeService(judgeRepository)
        judgeRoutes(judgeService)

        val courtRepository = CourtRepository(dataSource)
        val courtService = CourtService(courtRepository)
        courtRoutes(courtService)

        val judgementRepo = JudgementRepository(dataSource)
        val judgementService = JudgementService(judgementRepo)
        judgementRoutes(judgementService)

        val changeStatusRepo = JudgementStatusChangeRepository(dataSource)
        val changeStatusService = JudgementStatusChangeService(changeStatusRepo)
        judgementStatusChangeRoutes(changeStatusService)

        val costRepository = CostRepository(dataSource)
        val costService = CostService(costRepository)
        costRoutes(costService)
    }
}
