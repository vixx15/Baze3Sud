package ui.feature.cost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baze3.models.CostItemDTO
import com.baze3.models.CostSheetDTO
import com.baze3.models.CostSheetDetailsDTO
import com.baze3.models.CourtCaseDTO
import com.baze3.models.CourtCaseDetailsDTO
import com.baze3.models.CourtDTO
import com.baze3.models.DisputeTypeDTO
import com.baze3.models.JudgeDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import service.CostService
import service.CourtCaseService
import ui.common.ContentMode

class CostViewModel(
    private val costService: CostService = CostService(),
    private val courtCaseService: CourtCaseService = CourtCaseService(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<CostViewState>(CostViewState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun prepareData() {
        loadData()
        loadCases()
    }


    fun loadCases() {
        viewModelScope.launch {
            val res = courtCaseService.getAllCases()
            _uiState.update {
                it.copy(
                    caseOptions = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val res = costService.getAllSheets()
            _uiState.update {
                it.copy(
                    items = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun loadDetails(id: Long) {
        viewModelScope.launch {
            val res = costService.getFullDetails(id)
            _uiState.update {
                it.copy(
                    currentDetails = res.data ?: CostSheetDetailsDTO(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success,
                    newDetails = null, contentMode = ContentMode.OVERVIEW,
                    itemMode = ContentMode.OVERVIEW,
                    newItem = null,
                    currentItem = null,
                )
            }
        }
    }

    fun upsert() {
        viewModelScope.launch {
            val newDetails = _uiState.value.newDetails ?: return@launch
            val dto = CostSheetDetailsDTO(
                id = newDetails.id,
                date = newDetails.date,
                totalAmount = newDetails.totalAmount,
                caseNumber = newDetails.caseNumber,
                personalId = newDetails.personalId,
                items = newDetails.items,
            )

            val res = when (_uiState.value.contentMode) {
                ContentMode.EDIT -> costService.updateFullSheet(dto)
                ContentMode.INSERT -> costService.createSheet(dto)
                else -> null
            }

            res?.let { r ->
                _uiState.update {
                    it.copy(
                        dialogMessage = r.message, isError = !r.success
                    )
                }
                if (r.success) {
                    loadData()
                    newDetails.id?.let { loadDetails(it) }
                }
            }
        }
    }

    fun delete() {
        val current = _uiState.value.currentDetails
        val id = current?.id

        if (id != null) {
            viewModelScope.launch {
                val res = costService.deleteSheet(id)
                _uiState.update {
                    it.copy(
                        dialogMessage = res.message, isError = !res.success
                    )
                }
                if (res.success) loadData()
            }
        }
    }

    // Funkcija za zatvaranje dijaloga
    fun clearDialog() {
        _uiState.update { it.copy(dialogMessage = null, isError = false) }
    }

    fun update(
        id: Long? = null,
        date: Long? = null,
        total: Double? = null,
        caseId: String? = null,
        personalId: String? = null,
    ) {
        _uiState.update {
            val oldDetails = it.newDetails
            it.copy(
                newDetails = (oldDetails ?: CostSheetDetailsDTO()).copy(
                    id = id ?: oldDetails?.id,
                    date = date ?: oldDetails?.date,
                    totalAmount = total ?: oldDetails?.totalAmount,
                    caseNumber = caseId ?: oldDetails?.caseNumber,
                    personalId = personalId ?: oldDetails?.personalId,
                )
            )
        }
    }

    fun resetDetails() {
        _uiState.update { it.copy(contentMode = ContentMode.OVERVIEW, newDetails = null, currentDetails = null) }
    }

    fun updateContentMode(contentMode: ContentMode) {
        val new = when (contentMode) {
            ContentMode.EDIT -> _uiState.value.currentDetails
            ContentMode.OVERVIEW -> _uiState.value.currentDetails
            ContentMode.INSERT -> CostSheetDetailsDTO()
        }
        _uiState.update { it.copy(contentMode = contentMode, newDetails = new) }
    }

    fun deleteItem(ordinal: String) {
        _uiState.update {
            it.copy(newDetails = it.newDetails?.copy(items = _uiState.value.newDetails?.items?.filter { it.ordinalId == ordinal }
                ?: listOf()))
        }
    }


    fun updateItemMode(contentMode: ContentMode, item: CostItemDTO? = null) {
        val new = when (contentMode) {
            ContentMode.EDIT -> item
            ContentMode.OVERVIEW -> item
            ContentMode.INSERT -> CostItemDTO()
        }
        _uiState.update { it.copy(itemMode = contentMode, newItem = new) }
    }

    fun updateItem(
        sheetId: Long? = null,
        ordinalId: String? = null,
        type: String? = null,
        quantity: Double? = null,
        unitPrice: Double? = null,
        purpose: String? = null,
    ) {
        _uiState.update {
            val oldDetails = it.newItem
            it.copy(
                newItem = (oldDetails ?: CostItemDTO()).copy(
                    costSheetId = sheetId ?: oldDetails?.costSheetId,
                    ordinalId = ordinalId ?: oldDetails?.ordinalId,
                    type = type ?: oldDetails?.type,
                    unitPrice = unitPrice ?: oldDetails?.unitPrice,
                    quantity = quantity ?: oldDetails?.quantity,
                    purpose = purpose ?: oldDetails?.purpose
                )
            )
        }
    }

    fun upsertItem() {
        val newItem = _uiState.value.newItem
        if (newItem != null) {
            var currentItems = _uiState.value.currentDetails?.items?.toMutableList() ?: mutableListOf()
            if (currentItems.none { newItem.ordinalId == it.ordinalId }) {
                currentItems.add(newItem)
            } else {
                currentItems = currentItems.map {
                    if (newItem.ordinalId == it.ordinalId) {
                        newItem
                    } else {
                        it
                    }
                }.toMutableList()
            }
            _uiState.update { it.copy(newDetails = it.newDetails?.copy(items = currentItems)) }
        }
    }
}

data class CostViewState(
    val items: List<CostSheetDTO> = listOf(),
    val currentDetails: CostSheetDetailsDTO? = null,
    val currentItem: CostItemDTO? = null,
    val newDetails: CostSheetDetailsDTO? = null,
    val newItem: CostItemDTO? = null,
    val contentMode: ContentMode = ContentMode.OVERVIEW,
    val itemMode: ContentMode = ContentMode.OVERVIEW,
    val dialogMessage: String? = null,
    val isError: Boolean? = null,
    val caseOptions: List<CourtCaseDTO> = listOf(),
)