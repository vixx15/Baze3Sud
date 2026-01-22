package ui.feature.judge.overview

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
import com.baze3.models.JudgeDTO

@Composable
fun JudgeInfoComponent(
    modifier: Modifier = Modifier, judgeDTO: JudgeDTO, isSelected: Boolean,
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
            text = (judgeDTO.id?.toString() ?: "") + " ${judgeDTO.name}",
            color = MaterialTheme.colors.onPrimary
        )
    }
}