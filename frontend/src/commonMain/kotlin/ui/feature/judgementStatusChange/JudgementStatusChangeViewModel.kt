package ui.feature.judgementStatusChange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baze3.models.FirstJudgementStatusDTO
import com.baze3.models.JudgementDTO
import com.baze3.models.JudgementStatusChangeDTO
import com.baze3.models.JudgementStatusChangeDetailsDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import service.JudgementService
import service.JudgementStatusChangeService
import service.JudgementStatusService
import ui.common.ContentMode

class JudgementStatusChangeViewModel(
    private val firstJudgementStatusService: JudgementStatusService= JudgementStatusService(),
    private val judgementService: JudgementService= JudgementService(),
    private val judgementStatusChangeService: JudgementStatusChangeService= JudgementStatusChangeService()
) : ViewModel() {

    private val _uiState = MutableStateFlow<JudgementStatusChangeViewState>(JudgementStatusChangeViewState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun prepareData() {
        loadData()
        loadJudgements()
        loadStatusOptions()
    }

    fun loadJudgements() {
        viewModelScope.launch {
            val res = judgementService.getAll()
            _uiState.update {
                it.copy(
                    judgementOptions = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun loadStatusOptions() {
        viewModelScope.launch {
            val res = firstJudgementStatusService.getAll()
            _uiState.update {
                it.copy(
                    statusOptions = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val res = judgementStatusChangeService.getAll()
            _uiState.update {
                it.copy(
                    items = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun filterByJudgement(id: String) {
        viewModelScope.launch {
            val res = judgementStatusChangeService.getHistory(id)
            _uiState.update {
                it.copy(
                    filteredData = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun loadDetails(judgementId: String, statusId: Long) {
        viewModelScope.launch {
            val res = judgementStatusChangeService.getSingleDetails(judgementId, statusId)
            _uiState.update {
                it.copy(
                    currentDetails = res.data,
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success,
                    newDetails = null, contentMode = ContentMode.OVERVIEW
                )
            }
        }
    }

    fun upsert() {
        viewModelScope.launch {
            val newDetails = _uiState.value.newDetails ?: return@launch
            val dto = JudgementStatusChangeDTO(
                judgementId = newDetails.judgement?.id,
                statusId = newDetails.status?.id,
                date = newDetails.date,
                isCurrent = newDetails.isCurrent
            )

            val res = when (_uiState.value.contentMode) {
                ContentMode.EDIT -> judgementStatusChangeService.update(dto)
                ContentMode.INSERT -> judgementStatusChangeService.create(dto)
                else -> null
            }

            res?.let { r ->
                _uiState.update {
                    it.copy(
                        dialogMessage = r.message, isError = !r.success
                    )
                }
                if (r.success) loadData()
            }
        }
    }

    fun delete() {
        val current = _uiState.value.currentDetails
        val jId = current?.judgement?.id
        val sId = current?.status?.id

        if (jId != null && sId != null) {
            viewModelScope.launch {
                val res = judgementStatusChangeService.delete(jId, sId)
                _uiState.update {
                    it.copy(
                        dialogMessage = res.message, isError = !res.success
                    )
                }
                if (res.success) loadData()
            }
        }
    }

    fun clearDialog() {
        _uiState.update { it.copy(dialogMessage = null, isError = false) }
    }

    fun update(
        judgement: JudgementDTO? = null,
        status: FirstJudgementStatusDTO? = null,
        date: Long? = null,
        isCurrent: Boolean? = null,
    ) {
        _uiState.update {
            val oldDetails = it.newDetails
            it.copy(
                newDetails = (oldDetails ?: JudgementStatusChangeDetailsDTO()).copy(
                    judgement = judgement ?: oldDetails?.judgement,
                    status = status ?: oldDetails?.status,
                    date = date ?: oldDetails?.date,
                    isCurrent = isCurrent ?: oldDetails?.isCurrent,
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
            ContentMode.INSERT -> JudgementStatusChangeDetailsDTO()
        }
        _uiState.update { it.copy(contentMode = contentMode, newDetails = new) }
    }
}

data class JudgementStatusChangeViewState(
    val items: List<JudgementStatusChangeDTO> = listOf(),
    val currentDetails: JudgementStatusChangeDetailsDTO? = null,
    val newDetails: JudgementStatusChangeDetailsDTO? = null,
    val filteredData: List<JudgementStatusChangeDetailsDTO> = listOf(),
    val contentMode: ContentMode = ContentMode.OVERVIEW,
    val dialogMessage: String? = null,
    val isError: Boolean? = null,
    val judgementOptions: List<JudgementDTO> = listOf(),
    val statusOptions: List<FirstJudgementStatusDTO> = listOf(),
)