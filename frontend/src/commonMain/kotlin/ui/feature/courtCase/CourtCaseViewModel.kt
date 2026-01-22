package ui.feature.courtCase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baze3.models.CourtCaseDTO
import com.baze3.models.CourtCaseDetailsDTO
import com.baze3.models.CourtDTO
import com.baze3.models.DisputeTypeDTO
import com.baze3.models.JudgeDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import service.CourtCaseService
import service.CourtService
import service.DisputeTypeService
import service.JudgeService
import ui.common.ContentMode

class CourtCaseViewModel(
    private val judgeService: JudgeService = JudgeService(),
    private val courtService: CourtService = CourtService(),
    private val courtCaseService: CourtCaseService = CourtCaseService(),
    private val disputeTypeService: DisputeTypeService = DisputeTypeService(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<CourtCaseViewState>(CourtCaseViewState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun prepareData() {
        loadData()
        loadJudges()
        loadTypes()
        loadCourts()
    }

    fun loadJudges() {
        viewModelScope.launch {
            val res = judgeService.getAllJudges()
            _uiState.update {
                it.copy(
                    judgesOptions = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            val res = disputeTypeService.getAll()
            _uiState.update {
                it.copy(
                    typesOptions = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val res = courtCaseService.getAllCases()
            _uiState.update {
                it.copy(
                    items = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun loadDetails(id: String) {
        viewModelScope.launch {
            val res = courtCaseService.getCaseDetails(id)
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
            val dto = CourtCaseDTO(
                id = newDetails.id,
                value = newDetails.value,
                typeId = newDetails.type?.id,
                judgeId = newDetails.judge?.id,
                courtId = newDetails.court?.id
            )

            val res = when (_uiState.value.contentMode) {
                ContentMode.EDIT -> courtCaseService.updateCase(dto)
                ContentMode.INSERT -> courtCaseService.createCase(dto)
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
        val id = current?.id

        if (id != null) {
            viewModelScope.launch {
                val res = courtCaseService.deleteCase(id)
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
        id: String?,
        value: Double? = null,
        type: DisputeTypeDTO? = null,
        judge: JudgeDTO? = null,
        court: CourtDTO? = null,
    ) {
        _uiState.update {
            val oldDetails = it.newDetails
            it.copy(
                newDetails = (oldDetails ?: CourtCaseDetailsDTO()).copy(
                    id = id ?: oldDetails?.id,
                    judge = judge ?: oldDetails?.judge,
                    court = court ?: oldDetails?.court,
                    type = type ?: oldDetails?.type,
                    value = value ?: oldDetails?.value,
                )
            )
        }
    }

    fun resetDetails() {
        _uiState.update { it.copy(contentMode = ContentMode.OVERVIEW, newDetails = null, currentDetails = null) }
    }

    fun loadCourts() {
        viewModelScope.launch {
            val response = courtService.getAllCourts()
            _uiState.update {
                it.copy(
                    courtOptions = response.data ?: listOf(),
                    dialogMessage = if (!response.success) response.message else it.dialogMessage,
                    isError = !response.success
                )
            }

        }
    }

    fun updateContentMode(contentMode: ContentMode) {
        val new = when (contentMode) {
            ContentMode.EDIT -> _uiState.value.currentDetails
            ContentMode.OVERVIEW -> _uiState.value.currentDetails
            ContentMode.INSERT -> CourtCaseDetailsDTO()
        }
        _uiState.update { it.copy(contentMode = contentMode, newDetails = new) }
    }
}

data class CourtCaseViewState(
    val items: List<CourtCaseDTO> = listOf(),
    val currentDetails: CourtCaseDetailsDTO? = null,
    val newDetails: CourtCaseDetailsDTO? = null,
    val contentMode: ContentMode = ContentMode.OVERVIEW,
    val dialogMessage: String? = null,
    val isError: Boolean? = null,
    val courtOptions: List<CourtDTO> = listOf(),
    val judgesOptions: List<JudgeDTO> = listOf(),
    val typesOptions: List<DisputeTypeDTO> = listOf()
)