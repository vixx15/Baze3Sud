package ui.feature.courtCase.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baze3.models.CourtCaseDetailsDTO
import com.baze3.models.CourtDTO
import com.baze3.models.DisputeTypeDTO
import com.baze3.models.JudgeDTO
import ui.common.CustomTextField
import ui.common.DropDownMenu
import ui.common.ContentMode

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateCourtCaseComponent(
    mode: ContentMode,
    updateCourtCase: (
        id: String?,
        value: Double?,
        type: DisputeTypeDTO?,
        judge: JudgeDTO?,
        court: CourtDTO?,
    ) -> Unit,
    courtCaseDetailsDTO: CourtCaseDetailsDTO,
    courtOptions: List<CourtDTO>,
    judgeOptions: List<JudgeDTO>,
    diputeTypeOptions: List<DisputeTypeDTO>,
    confirm: () -> Unit,
) {
    Column(modifier = Modifier.Companion.padding(12.dp)) {
        CustomTextField(
            isPK = true,
            contentMode = mode,
            value = courtCaseDetailsDTO.id ?: "",
            onValueChange = { updateCourtCase(it, null, null, null, null) },
            label = "Broj predmeta",
            singleLine = true,
        )

        CustomTextField(
            isPK = false,
            contentMode = mode,
            value = courtCaseDetailsDTO.value?.toString() ?: "",
            onValueChange = { updateCourtCase(null, it.toDoubleOrNull() ?: 0.0, null, null, null) },
            label = "Vrednost spora",
            singleLine = true,
        )

        var expandedType by remember { mutableStateOf(false) }
        DropDownMenu(
            contentMode = mode,
            isPK = false,
            options = diputeTypeOptions,
            optionLabel = { it.name ?: "" },
            expanded = expandedType,
            menuLabel = "Vrsta spora",
            selectedOption = courtCaseDetailsDTO.type,
            updateExpandend = { expandedType = it },
            updateValue = { updateCourtCase(null, null, it, null, null) }
        )

        var expandedJudge by remember { mutableStateOf(false) }
        DropDownMenu(
            contentMode = mode,
            isPK = false,
            options = judgeOptions,
            optionLabel = { it.name ?: "" },
            expanded = expandedJudge,
            menuLabel = "Sudija",
            selectedOption = courtCaseDetailsDTO.judge,
            updateExpandend = { expandedJudge = it },
            updateValue = { updateCourtCase(null, null, null, it, null) }
        )

        var expandedCourt by remember { mutableStateOf(false) }
        DropDownMenu(
            contentMode = mode,
            isPK = false,
            options = courtOptions,
            optionLabel = { it.name ?: "" },
            expanded = expandedCourt,
            menuLabel = "Sud",
            selectedOption = courtCaseDetailsDTO.court,
            updateExpandend = { expandedCourt = it },
            updateValue = { updateCourtCase(null, null, null, null, it) }
        )

        Button(onClick = { confirm() }, modifier = Modifier.Companion.fillMaxWidth()) { Text("Potvrdi") }

    }
}