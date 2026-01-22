package ui.feature.cost

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
import com.baze3.models.CostSheetDTO
import com.baze3.models.CourtCaseDTO
import ui.feature.judgementStatusChange.details.toFormattedDate

@Composable
fun CostSheetInfoComponent(
    modifier: Modifier = Modifier, costSheetDTO: CostSheetDTO, isSelected: Boolean,
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
            text = "ID:" +(costSheetDTO.id?.toString() ?: "")+" Predat ${(costSheetDTO.date?.toFormattedDate() ?: "")}",
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = "Za predmet: " + costSheetDTO.caseNumber ,
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = "Ukupno: " + costSheetDTO.totalAmount +"RSD",
            color = MaterialTheme.colors.onPrimary
        )
    }
}