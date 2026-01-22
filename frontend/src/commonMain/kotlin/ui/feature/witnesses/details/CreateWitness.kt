package ui.feature.witnesses.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baze3.models.WitnessDTO
import ui.common.ContentMode
import ui.common.CustomTextField

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateWitness(
    mode: ContentMode,
    updateWitness: (
        id: String?,
        name: String?,
        place: String?,
        address: String?,
        mother: String?,
        father: String?,
    ) -> Unit,
    witness: WitnessDTO,
    confirm: () -> Unit,
) {
    Column(modifier = Modifier.Companion.padding(12.dp)) {
        CustomTextField(
            isPK = true,
            contentMode = mode,
            value = witness.id ?: "",
            onValueChange = { updateWitness(it, null, null, null, null, null) },
            label = "JMBG Svedoka",
            singleLine = true,
        )

        CustomTextField(
            isPK = false,
            contentMode = mode,
            value = witness?.name ?: "",
            onValueChange = { updateWitness(null, it, null, null, null, null) },
            label = "Puno Ime Svedoka",
            modifier = Modifier.Companion.fillMaxWidth().weight(1f),
            singleLine = true,
        )

        CustomTextField(
            isPK = false,
            contentMode = mode,
            value = witness?.place ?: "",
            onValueChange = { updateWitness(null, null, it, null, null, null) },
            label = "Mesto",
            modifier = Modifier.Companion.fillMaxWidth().weight(1f),
            singleLine = true,
        )
        CustomTextField(
            isPK = false,
            contentMode = mode,
            value = witness?.address ?: "",
            onValueChange = { updateWitness(null, null, null, it, null, null) },
            label = "Adresa",
            modifier = Modifier.Companion.fillMaxWidth().weight(1f),
            singleLine = true,
        )
        CustomTextField(
            isPK = false,
            contentMode = mode,
            value = witness?.mothersName ?: "",
            onValueChange = { updateWitness(null, null, null, null, it, null) },
            label = "Ime majke svedoka",
            modifier = Modifier.Companion.fillMaxWidth().weight(1f),
            singleLine = true,
        )

        CustomTextField(
            isPK = false,
            contentMode = mode,
            value = witness?.fathersName ?: "",
            onValueChange = { updateWitness(null, null, null, null, null, it) },
            label = "Ime oca svedoka",
            modifier = Modifier.Companion.fillMaxWidth().weight(1f),
            singleLine = true,
        )


        Button(onClick = { confirm() }, modifier = Modifier.Companion.fillMaxWidth()) { Text("Potvrdi") }

    }
}