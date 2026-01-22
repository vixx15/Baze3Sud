package ui.feature.cost

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baze3.models.CostItemDTO
import com.baze3.models.CostSheetDetailsDTO
import com.baze3.models.CourtCaseDetailsDTO
import com.baze3.models.CourtDTO
import com.baze3.models.DisputeTypeDTO
import com.baze3.models.JudgeDTO
import ui.common.CustomTextField
import ui.common.DropDownMenu
import ui.common.ContentMode
import ui.common.CustomDatePicker
import ui.feature.judgementStatusChange.details.toFormattedDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateCostSheetComponent(
    vm: CostViewModel
) {
    val viewState by vm.uiState.collectAsState()
    val mode = viewState.contentMode
    val details = if (mode == ContentMode.OVERVIEW) {
        viewState.currentDetails
    } else {
        viewState.newDetails ?: CostSheetDetailsDTO()
    }

    var itemPopupVisible by remember { mutableStateOf(false) }
    val itemMode = viewState.itemMode
    CreateCostItemPopup(
        mode = itemMode,
        show = itemPopupVisible,
        costItemDTO = viewState.newItem ?: CostItemDTO(),
        update = vm::updateItem,
        confirm = { vm.upsertItem() }
    ) { itemPopupVisible = it }

    if (details != null) {
        Column(modifier = Modifier.Companion.padding(12.dp)) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomTextField(
                    isPK = true,
                    contentMode = mode,
                    value = details?.id?.toString() ?: "",
                    onValueChange = { vm.update(id = it.toLongOrNull()) },
                    label = "ID",
                    singleLine = true,
                )

                var pickerVisible by remember { mutableStateOf(false) }
                CustomTextField(
                    modifier = Modifier.clickable { pickerVisible = true },
                    contentMode = mode,
                    isPK = false,
                    value = details?.date?.toFormattedDate() ?: "",
                    onValueChange = {
                    },
                    enabled = false,
                    label = "Datum",
                    singleLine = true
                )

                CustomDatePicker(
                    show = pickerVisible,
                    initialTime = details?.date,
                    updateVisibility = { pickerVisible = it },
                    onDateSelected = { vm.update(date = it) }
                )

                CustomTextField(
                    isPK = false,
                    contentMode = mode,
                    value = details?.totalAmount?.toString() ?: "",
                    onValueChange = { vm.update(total = it.toDoubleOrNull()) },
                    label = "Ukupno",
                    singleLine = true,
                )

                CustomTextField(
                    isPK = false,
                    contentMode = mode,
                    value = details?.caseNumber ?: "",
                    onValueChange = { vm.update(caseId = it) },
                    label = "Broj predmeta",
                    singleLine = true,
                )
            }
            Spacer(Modifier.height(16.dp).fillMaxWidth())

            Row(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Trosak"
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Kolicina"
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Cena"
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Ukupno"
                )
                if (mode != ContentMode.OVERVIEW) {
                    Button(modifier = Modifier.weight(1f), onClick = {
                        itemPopupVisible = true
                        vm.updateItemMode(ContentMode.INSERT)
                    }) { Text("Dodaj novi trosak") }
                }
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            LazyColumn(Modifier.fillMaxWidth().height(300.dp)) {
                details?.items?.forEach {
                    item {
                        CostItem(
                            Modifier.padding(vertical = 4.dp), it,
                            delete = { it.ordinalId?.let { ordinal -> vm.deleteItem(ordinal) } },
                            edit = {
                                itemPopupVisible = true
                                vm.updateItemMode(ContentMode.EDIT, it)
                            },
                            allowEdit = mode != ContentMode.OVERVIEW,
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp).fillMaxWidth())
            if (mode != ContentMode.OVERVIEW)
                Button(onClick = { vm.upsert() }, modifier = Modifier.Companion.fillMaxWidth()) { Text("Potvrdi") }
        }


    }
}

@Composable
fun CostItem(modifier: Modifier, costItemDTO: CostItemDTO, delete: () -> Unit, edit: () -> Unit, allowEdit: Boolean) {
    Row(modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.weight(1f),
            text = "${costItemDTO.ordinalId}. ${costItemDTO.type} trosak za ${costItemDTO.purpose}"
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${costItemDTO.quantity}"
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${costItemDTO.unitPrice} RSD"
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${(costItemDTO.unitPrice ?: 0.0) * (costItemDTO.quantity ?: 0.0)} RSD"
        )

        if (allowEdit) {
            Column(modifier = Modifier.weight(1f)) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = edit) { Text("Izmeni") }
                Button(modifier = Modifier.fillMaxWidth(), onClick = delete) { Text("Obrisi") }
            }
        }
    }
}