package ui.feature.judgementStatusChange.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.baze3.models.CourtCaseDTO
import ui.feature.judgementStatusChange.details.toFormattedDate

@Composable
fun JudgementStatusChangeInfoComponent(
    modifier: Modifier = Modifier,
    judgementText: String,
    isSelected: Boolean,
    date: Long,
    isCurrent: Boolean,
    statusText: String
) {
    Column(
        modifier.background(
            if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
            shape = RoundedCornerShape(10.dp)
        ).border(
            3.dp, if (isCurrent) Color.Green
            else MaterialTheme.colors.primary,
            shape = RoundedCornerShape(10.dp)
        )
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = (judgementText),
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = statusText,
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = date.toFormattedDate(),
            color = MaterialTheme.colors.onPrimary
        )
    }
}