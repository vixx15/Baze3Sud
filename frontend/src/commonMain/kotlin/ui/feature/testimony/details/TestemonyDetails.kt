package ui.feature.testimony.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.baze3.models.TestimonyDetailsDTO
import ui.feature.testimony.overview.HearingInfo
import ui.feature.testimony.overview.WitnessInfo

@Composable
fun TestemonyDetails(detailsDTO: TestimonyDetailsDTO) {
    Column(Modifier.Companion.fillMaxWidth().padding(12.dp)) {
        Text(
            modifier = Modifier.Companion.fillMaxWidth().padding(bottom = 8.dp),
            text = "Svedocenje ${detailsDTO.witnessName}",
            textAlign = TextAlign.Companion.Center
        )
        Row(Modifier.Companion.padding(bottom = 16.dp)) {
            detailsDTO.hearing?.let { HearingInfo(modifier = Modifier.Companion.weight(1f), it) }
            Spacer(Modifier.Companion.width(12.dp))
            detailsDTO.witness?.let { WitnessInfo(modifier = Modifier.Companion.weight(1f), it) }
        }
        Text(
            modifier = Modifier.Companion.fillMaxWidth(), text = detailsDTO.content ?: ""
        )
    }
}
