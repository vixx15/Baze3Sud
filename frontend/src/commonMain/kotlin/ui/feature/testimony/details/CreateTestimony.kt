package ui.feature.testimony.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baze3.models.HearingDTO
import com.baze3.models.TestimonyDetailsDTO
import com.baze3.models.WitnessDTO
import ui.common.ContentMode
import ui.common.CustomTextField
import ui.common.DropDownMenu

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateTestimony(
    witnesses: List<WitnessDTO>,
    hearingOptions: List<HearingDTO>,
    mode: ContentMode,
    updateTestimony: (
        hearingDTO: HearingDTO?, witnessDTO: WitnessDTO?, witnessName: String?, content: String?
    ) -> Unit,
    testimony: TestimonyDetailsDTO,
    confirm: () -> Unit,
) {
    var expandedSvedok by remember { mutableStateOf(false) }
    var expandedCourtCase by remember { mutableStateOf(false) }
    Column(modifier = Modifier.Companion.padding(12.dp)) {
        DropDownMenu(
            isPK = true,
            contentMode = mode,
            options = witnesses,
            optionLabel = {
                it?.name ?: ""
            },
            expanded = expandedSvedok,
            menuLabel = "Svedok",
            selectedOption = testimony.witness,
            updateExpandend = { expandedSvedok = it },
            updateValue = { updateTestimony(null, it, null, null) })

        CustomTextField(
            isPK = false,
            contentMode = mode,
            value = testimony.witnessName ?: "",
            onValueChange = { updateTestimony(null, null, it, null) },
            label = "Ime svedoka",
            singleLine = true,
        )

        DropDownMenu(
            isPK = true,
            contentMode = mode,
            options = hearingOptions,
            optionLabel = {
                "Rasprava ${it.id} za predmet ${it.caseId}"
            },
            expanded = expandedCourtCase,
            menuLabel = "Predmet",
            selectedOption = testimony.hearing,
            updateExpandend = { expandedCourtCase = it },
            updateValue = { updateTestimony(it, null, null, null) })

        CustomTextField(
            isPK = false,
            contentMode = mode,
            value = testimony?.content ?: "",
            onValueChange = { updateTestimony(null, null, null, it) },
            label = "Kompletan iskaz svedoka",
            modifier = Modifier.Companion.fillMaxWidth().weight(1f),
            singleLine = false,
        )

        Button(onClick = { confirm() }, modifier = Modifier.Companion.fillMaxWidth()) { Text("Potvrdi") }

    }
}