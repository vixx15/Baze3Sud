package ui.feature.judge.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baze3.models.CourtDTO
import com.baze3.models.JudgeDetailsDTO
import ui.common.ContentMode
import ui.common.CustomTextField
import ui.common.DropDownMenu

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateJudgeComponent(
    mode: ContentMode,
    updateJudge: (
        id: Long?,
        name: String?,
        court: CourtDTO?,
    ) -> Unit,
    judgeDetailsDTO: JudgeDetailsDTO,
    courtOptions: List<CourtDTO>,
    confirm: () -> Unit,
) {
    Column(modifier = Modifier.Companion.padding(12.dp)) {
        CustomTextField(
            isPK = true,
            contentMode = mode,
            value = judgeDetailsDTO.id?.toString() ?: "",
            onValueChange = { updateJudge(it.toLongOrNull(), null, null) },
            label = "ID sudije",
            singleLine = true,
        )

        CustomTextField(
            isPK = false,
            contentMode = mode,
            value = judgeDetailsDTO.name ?: "",
            onValueChange = { updateJudge(null, it, null) },
            label = "Ime sudije",
            singleLine = true,
        )

        var expanded by remember { mutableStateOf(false) }
        DropDownMenu(
            contentMode = mode,
            isPK = false,
            options = courtOptions,
            optionLabel = { it.name ?: "" },
            expanded = expanded,
            menuLabel = "Sud",
            selectedOption = judgeDetailsDTO.court,
            updateExpandend = { expanded = it },
            updateValue = { updateJudge(null, null, it) }
        )

        Button(onClick = { confirm() }, modifier = Modifier.Companion.fillMaxWidth()) { Text("Potvrdi") }

    }
}