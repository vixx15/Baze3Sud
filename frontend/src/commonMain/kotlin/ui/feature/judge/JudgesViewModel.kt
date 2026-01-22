package ui.feature.judge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baze3.models.CourtDTO
import com.baze3.models.JudgeDTO
import com.baze3.models.JudgeDetailsDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import service.CourtService
import service.JudgeService
import ui.common.ContentMode

class JudgesViewModel(
    private val judgeService: JudgeService = JudgeService(),
    private val courtService: CourtService = CourtService(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<JudgeViewState>(JudgeViewState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val res = judgeService.getAllJudges()
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
            val res = judgeService.getJudgeDetails(id)
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
            val dto = JudgeDTO(
                id = newDetails.id, name = newDetails.name, courtId = newDetails.court?.id
            )

            val res = when (_uiState.value.contentMode) {
                ContentMode.EDIT -> judgeService.updateJudge(dto)
                ContentMode.INSERT -> judgeService.createJudge(dto)
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
                val res = judgeService.deleteJudge(id)
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
        id: Long? = null, name: String? = null, court: CourtDTO? = null
    ) {
        _uiState.update {
            val oldDetails = it.newDetails
            it.copy(
                newDetails = (oldDetails ?: JudgeDetailsDTO()).copy(
                    id = id ?: oldDetails?.id,
                    name = name ?: oldDetails?.name,
                    court = court ?: oldDetails?.court,
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
        val newTestimony = when (contentMode) {
            ContentMode.EDIT -> _uiState.value.currentDetails
            ContentMode.OVERVIEW -> _uiState.value.currentDetails
            ContentMode.INSERT -> JudgeDetailsDTO()
        }
        _uiState.update { it.copy(contentMode = contentMode, newDetails = newTestimony) }
    }
}

data class JudgeViewState(
    val items: List<JudgeDTO> = listOf(),
    val currentDetails: JudgeDetailsDTO? = null,
    val newDetails: JudgeDetailsDTO? = null,
    val contentMode: ContentMode = ContentMode.OVERVIEW,
    val dialogMessage: String? = null,
    val isError: Boolean? = null,
    val courtOptions: List<CourtDTO> = listOf(),
)