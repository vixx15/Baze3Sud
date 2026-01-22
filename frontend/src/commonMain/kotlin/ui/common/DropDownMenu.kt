package ui.common

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> DropDownMenu(
    contentMode: ContentMode,
    isPK: Boolean,
    options: List<T>,
    optionLabel: (T) -> String,
    expanded: Boolean,
    menuLabel: String,
    selectedOption: T?,
    updateExpandend: (Boolean) -> Unit,
    updateValue: (T) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = {
            if (contentMode == ContentMode.INSERT || (contentMode == ContentMode.EDIT && !isPK)) {
                updateExpandend(!expanded)
            }
        }) {
        TextField(
            readOnly = true,
            value = selectedOption?.let { optionLabel(it) } ?: "",
            onValueChange = {},
            label = { Text(menuLabel) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { updateExpandend(false) }) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        updateValue(selectionOption)
                        updateExpandend(false)
                    }) {
                    Text(text = selectionOption?.let { optionLabel(it) } ?: "")
                }
            }
        }
    }
}