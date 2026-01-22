package ui.feature.judgementStatusChange.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baze3.models.FirstJudgementStatusDTO
import com.baze3.models.JudgementDTO
import com.baze3.models.JudgementStatusChangeDetailsDTO
import ui.common.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateJudgementChangeStateComponent(
    mode: ContentMode,
    updateJudgementStateChange: (
        judgement: JudgementDTO?,
        statusId: FirstJudgementStatusDTO?,
        date: Long?,
        isCurrent: Boolean?,
    ) -> Unit,
    judgementStatusChangeDetailsDTO: JudgementStatusChangeDetailsDTO,
    judgmentOptions: List<JudgementDTO>,
    judgementStatusOptions: List<FirstJudgementStatusDTO>,
    confirm: () -> Unit,
) {
    Column(modifier = Modifier.Companion.padding(12.dp)) {
        var expandedJudgement by remember { mutableStateOf(false) }
        DropDownMenu(
            contentMode = mode,
            isPK = true,
            options = judgmentOptions,
            optionLabel = { "Presuda ${it.id} za predmet ${it.caseID} dana ${it.date.toFormattedDate()}" },
            expanded = expandedJudgement,
            menuLabel = "Presuda",
            selectedOption = judgementStatusChangeDetailsDTO.judgement,
            updateExpandend = { expandedJudgement = it },
            updateValue = { updateJudgementStateChange(it, null, null, null) }
        )

        var expandedStatus by remember { mutableStateOf(false) }
        DropDownMenu(
            contentMode = mode,
            isPK = true,
            options = judgementStatusOptions,
            optionLabel = { it.name },
            expanded = expandedStatus,
            menuLabel = "Status",
            selectedOption = judgementStatusChangeDetailsDTO.status,
            updateExpandend = { expandedStatus = it },
            updateValue = { updateJudgementStateChange(null, it, null, null) }
        )

        var pickerVisible by remember { mutableStateOf(false) }
        CustomTextField(
            modifier = Modifier.clickable { pickerVisible = true },
            contentMode = mode,
            isPK = false,
            value = judgementStatusChangeDetailsDTO.date.toFormattedDate(),
            onValueChange = {
            },
            enabled = false,
            label = "Datum",
            singleLine = true
        )

        CustomDatePicker(
            show = pickerVisible,
            initialTime = judgementStatusChangeDetailsDTO.date,
            updateVisibility = { pickerVisible = it },
            onDateSelected = { updateJudgementStateChange(null, null, it, null) }
        )

        CustomSwitch(
            toggled = judgementStatusChangeDetailsDTO.isCurrent ?: false,
            label = "Trenutni",
            onCheckedChange = { updateJudgementStateChange(null, null, null, it) }
        )

        Button(onClick = { confirm() }, modifier = Modifier.Companion.fillMaxWidth()) { Text("Potvrdi") }
    }
}

fun Long?.toFormattedDate(pattern: String = "dd.MM.yyyy."): String {
    if (this == null) return "/"
    val date = Date(this)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}