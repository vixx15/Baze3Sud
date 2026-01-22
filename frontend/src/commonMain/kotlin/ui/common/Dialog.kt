package ui.common

import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleMessageDialog(
    title: String,
    message: String,
    isError: Boolean = false,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(
                text = title,
                color = if (isError) Color(0xFFB00020) else Color(0xFF388E3C),
                style = MaterialTheme.typography.h6
            )
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onClose) {
                Text("Zatvori")
            }
        },
        modifier = Modifier.width(350.dp)
    )
}