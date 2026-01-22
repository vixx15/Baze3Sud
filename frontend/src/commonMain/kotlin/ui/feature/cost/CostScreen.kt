package ui.feature.cost

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.common.ContentMode
import ui.common.SimpleMessageDialog

@Composable
fun CostScreen(){
    val vm = viewModel {
        CostViewModel()
    }
    val viewState by vm.uiState.collectAsState()

    viewState.dialogMessage?.let {
        SimpleMessageDialog(
            title = "Troskovi",
            message = it,
            isError = viewState.isError == true,
            onClose = vm::clearDialog
        )
    }

    LaunchedEffect(Unit) {
        vm.prepareData()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            text = "Predmeti",
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { vm.updateContentMode(ContentMode.INSERT) }) { Text("Novi") }
            if (viewState.contentMode == ContentMode.OVERVIEW && viewState.currentDetails != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.updateContentMode(ContentMode.EDIT) }) { Text("Izmeni") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.delete() }) { Text("Obrisi") }
            }
            if (viewState.currentDetails != null || viewState.contentMode != ContentMode.OVERVIEW) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { vm.resetDetails() }) { Text("Zatvori") }
            }
        }


        Row(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxHeight().widthIn(0.dp, 200.dp)) {
                items(items = viewState.items) {
                    CostSheetInfoComponent(
                        modifier = Modifier.clickable { it.id?.let { id -> vm.loadDetails(id) } }.padding(8.dp),
                        costSheetDTO = it,
                        isSelected = viewState.currentDetails?.id == it.id
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight().width(2.dp).background(MaterialTheme.colors.secondary))

           CreateCostSheetComponent(vm)
        }
    }
}