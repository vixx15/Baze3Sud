package ui.feature.cost

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.baze3.models.CostItemDTO
import ui.common.ContentMode
import ui.common.CustomTextField

@Composable
fun CreateCostItemPopup(
    mode: ContentMode,
    show: Boolean,
    costItemDTO: CostItemDTO,
    update: (
        sheetId: Long?,
        ordinalId: String?,
        type: String?,
        quantity: Double?,
        unitPrice: Double?,
        purpose: String?
    ) -> Unit,
    confirm: () -> Unit,
    updateVisibility: (Boolean) -> Unit,
) {
    if (show) {
        DialogWindow(
            onCloseRequest = { updateVisibility(false) },
            title = "Izaberite datum",
            state = rememberDialogState(width = 400.dp, height = 500.dp)
        ) {
            Column {
            CustomTextField(
                contentMode = mode,
                isPK = true,
                value = costItemDTO.costSheetId?.toString() ?: "",
                onValueChange = { update(it.toLongOrNull(), null, null, null, null, null) },
                label = "Broj Troskovnika",
            )
            CustomTextField(
                contentMode = mode,
                isPK = true,
                value = costItemDTO.ordinalId?.toString() ?: "",
                onValueChange = { update(null, it, null, null, null, null) },
                label = "Redni Broj",
            )

            CustomTextField(
                contentMode = mode,
                isPK = false,
                value = costItemDTO.type ?: "",
                onValueChange = { update(null, null, it, null, null, null) },
                label = "Vrsta",
            )

            CustomTextField(
                contentMode = mode,
                isPK = false,
                value = costItemDTO.quantity?.toString() ?: "",
                onValueChange = { update(null, null, null, it.toDoubleOrNull(), null, null) },
                label = "Broj jedinica",
            )

            CustomTextField(
                contentMode = mode,
                isPK = false,
                value = costItemDTO.unitPrice?.toString() ?: "",
                onValueChange = { update(null, null, null, null, it.toDoubleOrNull(), null) },
                label = "Cena jedinice",
            )

            CustomTextField(
                contentMode = mode,
                isPK = false,
                value = costItemDTO.purpose ?: "",
                onValueChange = { update(null, null, null, null, null, it) },
                label = "Namena",
            )

            TextButton(
                onClick = {
                    updateVisibility(false)
                    confirm()
                },
                modifier = Modifier
            ) {
                Text("Potvrdi")
            }}
        }
    }
}