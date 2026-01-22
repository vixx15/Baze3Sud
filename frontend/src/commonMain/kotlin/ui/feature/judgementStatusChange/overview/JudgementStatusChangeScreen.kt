package ui.feature.judgementStatusChange.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.baze3.models.CourtCaseDetailsDTO
import com.baze3.models.JudgementStatusChangeDetailsDTO
import ui.common.SimpleMessageDialog
import ui.feature.courtCase.CourtCaseViewModel
import ui.feature.courtCase.details.CreateCourtCaseComponent
import ui.common.ContentMode
import ui.common.CustomTextField
import ui.feature.judgementStatusChange.JudgementStatusChangeViewModel
import ui.feature.judgementStatusChange.details.CreateJudgementChangeStateComponent

@Composable
fun JudgementStatusChangeScreen() {
    val vm = viewModel {
        JudgementStatusChangeViewModel()
    }
    val viewState by vm.uiState.collectAsState()

    viewState.dialogMessage?.let {
        SimpleMessageDialog(
            title = "Statusi presuda",
            message = it,
            isError = viewState.isError == true,
            onClose = vm::clearDialog
        )
    }

    LaunchedEffect(Unit) {
        vm.prepareData()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            text = "Statusi presuda",
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { vm.updateContentMode(ContentMode.INSERT) }) { Text("Nova") }
            if (viewState.contentMode == ContentMode.OVERVIEW && viewState.currentDetails != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.updateContentMode(ContentMode.EDIT) }) { Text("Izmeni") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.delete() }) { Text("Obrisi") }
            }
            if (viewState.currentDetails != null || viewState.contentMode != ContentMode.OVERVIEW) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.resetDetails() }) { Text("Zatvori") }
            }
        }


        Row(modifier = Modifier.fillMaxWidth()) {
            var query by remember { mutableStateOf("") }
            LazyColumn(modifier = Modifier.fillMaxHeight().widthIn(0.dp, 200.dp)) {
                item {
                    CustomTextField(isPK = false, contentMode = ContentMode.INSERT, value = query, onValueChange = {
                        query = it
                        if (query.isNotEmpty()) {
                            vm.filterByJudgement(query)
                        }
                    }, label = "Pretrazi")
                }
                if (query.isNotEmpty()) {
                    items(items = viewState.filteredData) {
                        val sid = it.status?.id
                        val jid = it.judgement?.id
                        JudgementStatusChangeInfoComponent(
                            modifier = Modifier.clickable {
                                if (sid != null && jid != null) {
                                    vm.loadDetails(
                                        jid, sid
                                    )
                                }
                            }.padding(8.dp),
                            judgementText = "Presuda broj ${it.judgement?.id}, za predmet ${it.judgement?.caseID}",
                            isSelected = sid == viewState.currentDetails?.status?.id && jid == viewState.currentDetails?.judgement?.id,
                            date = it.date ?: 0,
                            isCurrent = it.isCurrent ?: false,
                            statusText = "Status broj ${it.status?.id} ${it.status?.name}",
                        )
                    }
                } else {
                    items(items = viewState.items) {
                        val sid = it.statusId
                        val jid = it.judgementId
                        JudgementStatusChangeInfoComponent(
                            modifier = Modifier.clickable {
                                if (sid != null && jid != null) {
                                    vm.loadDetails(
                                        jid, sid
                                    )
                                }
                            }.padding(8.dp),
                            judgementText = "Presuda broj ${it.judgementId}, ",
                            isSelected = sid == viewState.currentDetails?.status?.id && jid == viewState.currentDetails?.judgement?.id,
                            date = it.date ?: 0,
                            isCurrent = it.isCurrent ?: false,
                            statusText = "Status broj ${it.statusId}",
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight().width(2.dp).background(MaterialTheme.colors.secondary))

            when (viewState.contentMode) {
                ContentMode.EDIT -> {
                    CreateJudgementChangeStateComponent(
                        mode = viewState.contentMode,
                        confirm = vm::upsert,
                        updateJudgementStateChange = vm::update,
                        judgementStatusChangeDetailsDTO = viewState.newDetails ?: JudgementStatusChangeDetailsDTO(),
                        judgmentOptions = viewState.judgementOptions,
                        judgementStatusOptions = viewState.statusOptions,

                        )
                }

                ContentMode.OVERVIEW -> {
                    viewState.currentDetails?.let {
                        CreateJudgementChangeStateComponent(
                            mode = viewState.contentMode,
                            confirm = {},
                            updateJudgementStateChange = vm::update,
                            judgementStatusChangeDetailsDTO =it,
                            judgmentOptions = viewState.judgementOptions,
                            judgementStatusOptions = viewState.statusOptions,

                            )
                    }
                }

                ContentMode.INSERT -> {
                    CreateJudgementChangeStateComponent(
                        mode = viewState.contentMode,
                        confirm = vm::upsert,
                        updateJudgementStateChange = vm::update,
                        judgementStatusChangeDetailsDTO = viewState.newDetails ?: JudgementStatusChangeDetailsDTO(),
                        judgmentOptions = viewState.judgementOptions,
                        judgementStatusOptions = viewState.statusOptions,

                        )
                }
            }
        }
    }
}