package ui.feature.courtCase.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baze3.models.CourtCaseDTO

@Composable
fun CourtCaseInfoComponent(
    modifier: Modifier = Modifier, courtCaseDTO: CourtCaseDTO, isSelected: Boolean,
) {
    Column(
        modifier.background(
            if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
            shape = RoundedCornerShape(10.dp)
        )
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = (courtCaseDTO.id?.toString() ?: ""),
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = "Vrednost spora: " + courtCaseDTO.value.toString() +" RSD",
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = "Id vrste spora: " + courtCaseDTO.typeId.toString(),
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = "Id sudije: " + courtCaseDTO.judgeId.toString(),
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = "Id suda: " + courtCaseDTO.courtId.toString(),
            color = MaterialTheme.colors.onPrimary
        )
    }
}