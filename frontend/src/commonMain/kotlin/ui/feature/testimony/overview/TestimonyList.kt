package ui.feature.testimony.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.baze3.models.HearingDTO
import com.baze3.models.TestimonyDetailsDTO
import com.baze3.models.WitnessDTO
import ui.common.ContentMode
import ui.common.SimpleMessageDialog
import ui.feature.testimony.TestimonyViewModel
import ui.feature.testimony.details.CreateTestimony
import ui.feature.testimony.details.TestemonyDetails
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TestimoniesScreen() {
    val vm = viewModel {
        TestimonyViewModel()
    }
    val viewState by vm.uiState.collectAsState()
    LaunchedEffect(Unit) {
        vm.loadData()
        vm.getAllWitnesses()
        vm.getAllHearings()
    }

    viewState.dialogMessage?.let {
        SimpleMessageDialog(
            title = "Svedocenje",
            message = it,
            isError = viewState.isError == true,
            onClose = vm::clearDialog
        )
    }

    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            text = "Svedocenja",
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { vm.updateContentMode(ContentMode.INSERT) }) { Text("Novi") }
            if (viewState.contentMode == ContentMode.OVERVIEW && viewState.currentTestimony != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.updateContentMode(ContentMode.EDIT) }) { Text("Izmeni") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.deleteTestimony() }) { Text("Obrisi") }
            }
            if (viewState.currentTestimony != null || viewState.contentMode != ContentMode.OVERVIEW) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.resetDetails() }) { Text("Zatvori") }
            }
        }


        Row(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxHeight().widthIn(0.dp, 200.dp)) {
                items(items = viewState.testimonies) {
                    Column(Modifier.clickable {
                        if (it.hearingId != null && it.witnessId != null) {
                            vm.loadDetails(it.hearingId!!, it.witnessId!!)
                        }
                    }.padding(4.dp)) {
                        Text(text = it.witnessName ?: "")
                        Text(text = it.content ?: "", maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                    Spacer(Modifier.width(200.dp).height(1.dp).background(MaterialTheme.colors.primary))
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight().width(2.dp).background(MaterialTheme.colors.secondary))

            when (viewState.contentMode) {
                ContentMode.EDIT -> {
                    CreateTestimony(
                        witnesses = viewState.witnessOptions ?: listOf(),
                        hearingOptions = viewState.hearingOptions,
                        mode = viewState.contentMode,
                        testimony = viewState.newTestimony ?: TestimonyDetailsDTO(),
                        updateTestimony = vm::updateTestimony,
                        confirm = vm::upsertTestimony,
                    )
                }

                ContentMode.OVERVIEW -> {
                    viewState.currentTestimony?.let {
                        TestemonyDetails(it)
                    }
                }

                ContentMode.INSERT -> {
                    CreateTestimony(
                        witnesses = viewState.witnessOptions ?: listOf(),
                        hearingOptions = viewState.hearingOptions,
                        mode = viewState.contentMode,
                        testimony = viewState.newTestimony ?: TestimonyDetailsDTO(),
                        updateTestimony = vm::updateTestimony,
                        confirm = vm::upsertTestimony
                    )
                }
            }

        }
    }
}

@Composable
fun HearingInfo(
    modifier: Modifier = Modifier, hearingDTO: HearingDTO
) {
    Column(modifier.background(MaterialTheme.colors.primary, shape = RoundedCornerShape(10.dp)).padding(8.dp)) {
        Text(
            text = "Rasprava za predmet ${hearingDTO.caseId} odrzana ${hearingDTO.time?.toDateTimeString() ?: ""}",
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun WitnessInfo(
    modifier: Modifier = Modifier, witnessDTO: WitnessDTO
) {
    Column(modifier.background(MaterialTheme.colors.primary, shape = RoundedCornerShape(10.dp)).padding(8.dp)) {
        Text(
            text = (witnessDTO.name ?: "") + " (${witnessDTO.fathersName}, ${witnessDTO.mothersName})",
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = witnessDTO.place + " " + witnessDTO.address, color = MaterialTheme.colors.onPrimary
        )
        Text(
            "JMBG: ${witnessDTO.id}", color = MaterialTheme.colors.onPrimary
        )
    }
}


fun Long.toDateTimeString(
    pattern: String = "dd.MM.yyyy HH:mm", zoneId: ZoneId = ZoneId.systemDefault()
): String = Instant.ofEpochMilli(this).atZone(zoneId).format(DateTimeFormatter.ofPattern(pattern))