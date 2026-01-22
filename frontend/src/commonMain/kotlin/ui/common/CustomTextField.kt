package ui.common

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier.Companion,
    contentMode: ContentMode,
    isPK: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean = true,
    singleLine: Boolean = true,
) {
    val isEnabled = contentMode == ContentMode.INSERT || (contentMode == ContentMode.EDIT /*&& !isPK*/)
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        enabled = enabled && isEnabled,
        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
    )
}

