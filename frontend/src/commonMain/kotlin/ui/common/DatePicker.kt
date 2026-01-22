package ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState

@Composable
fun CustomDatePicker(
    show: Boolean,
    initialTime: Long?,
    updateVisibility: (Boolean) -> Unit,
    onDateSelected: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialTime)
    val selectedDate = datePickerState.selectedDateMillis

    if (show) {
        DialogWindow(
            onCloseRequest = { updateVisibility(false) },
            title = "Izaberite datum",
            state = rememberDialogState(width = 400.dp, height = 500.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
            DatePicker(state = datePickerState)
            TextButton(
                onClick = {
                    onDateSelected(selectedDate)
                    updateVisibility(false)
                },
                modifier = Modifier
            ) {
                Text("Potvrdi")
            } }
        }
    }
}