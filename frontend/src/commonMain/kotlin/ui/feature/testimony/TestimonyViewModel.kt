package ui.feature.testimony

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baze3.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import service.CourtCaseService
import service.HearingService
import service.TestimonyRemoteService
import service.WitnessService
import ui.common.ContentMode

class TestimonyViewModel(
    private val testimonyService: TestimonyRemoteService = TestimonyRemoteService(),
    private val witnessService: WitnessService = WitnessService(),
    private val courtCaseService: CourtCaseService = CourtCaseService(),
    private val hearingService: HearingService = HearingService()
) : ViewModel() {

    private val _uiState = MutableStateFlow<TestimonyViewState>(TestimonyViewState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val res = testimonyService.getTestimonySummaries()
            _uiState.update {
                it.copy(
                    testimonies = res.data ?: emptyList(),
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun loadDetails(hearingId: Long, witnessId: String) {
        viewModelScope.launch {
            val res = testimonyService.getTestimonyDetail(hId = hearingId, wId = witnessId)
            _uiState.update {
                it.copy(
                    currentTestimony = res.data,
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success
                )
            }
        }
    }

    fun upsertTestimony() {
        viewModelScope.launch {
            val newTestimony = _uiState.value.newTestimony ?: return@launch
            val dto = TestimonyDTO(
                hearingId = newTestimony.hearing?.id,
                witnessId = newTestimony.witness?.id,
                content = newTestimony.content,
                witnessName = newTestimony.witnessName
            )

            val res = when (_uiState.value.contentMode) {
                ContentMode.EDIT -> testimonyService.updateTestimony(dto)
                ContentMode.INSERT -> testimonyService.createTestimony(dto)
                else -> null
            }

            res?.let { r ->
                _uiState.update {
                    it.copy(
                        dialogMessage = r.message,
                        isError = !r.success
                    )
                }
                if (r.success) loadData()
            }
        }
    }

    fun deleteTestimony() {
        val current = _uiState.value.currentTestimony
        val hId = current?.hearing?.id
        val wId = current?.witness?.id

        if (hId != null && wId != null) {
            viewModelScope.launch {
                val res = testimonyService.deleteTestimony(hId, wId)
                _uiState.update {
                    it.copy(
                        dialogMessage = res.message,
                        isError = !res.success
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

    fun updateTestimony(
        hearingDTO: HearingDTO? = null,
        witnessDTO: WitnessDTO? = null,
        witnessName: String? = null,
        content: String? = null
    ) {
        _uiState.update {
            val oldTestimony = it.newTestimony
            it.copy(
                newTestimony = (oldTestimony ?: TestimonyDetailsDTO()).copy(
                    hearing = hearingDTO ?: oldTestimony?.hearing,
                    witness = witnessDTO ?: oldTestimony?.witness,
                    content = content ?: oldTestimony?.content,
                    witnessName = witnessName ?: oldTestimony?.witnessName,
                )
            )
        }
    }

    fun getAllWitnesses() {
        viewModelScope.launch {
            val response = witnessService.fetchAllWitnesses()
            _uiState.update { it.copy(
                witnessOptions = response.data ?: emptyList(),
                dialogMessage = if (!response.success) response.message else it.dialogMessage,
                isError = !response.success
            ) }
        }
    }


    fun getAllHearings() {
        viewModelScope.launch {
            val response = hearingService.fetchAllHearings()
            _uiState.update { it.copy(
                hearingOptions = response.data ?: emptyList(),
                dialogMessage = if (!response.success) response.message else it.dialogMessage,
                isError = !response.success
            ) }
        }
    }

    fun resetDetails() {
        _uiState.update { it.copy(contentMode = ContentMode.OVERVIEW, newTestimony = null, currentTestimony = null) }
    }

    fun updateContentMode(contentMode: ContentMode) {
        val newTestimony = when (contentMode) {
            ContentMode.EDIT -> _uiState.value.currentTestimony
            ContentMode.OVERVIEW -> _uiState.value.currentTestimony
            ContentMode.INSERT -> TestimonyDetailsDTO()
        }
        _uiState.update { it.copy(contentMode = contentMode, newTestimony = newTestimony) }
    }
}

data class TestimonyViewState(
    val testimonies: List<TestimonyDTO> = listOf(),
    val currentTestimony: TestimonyDetailsDTO? = null,
    val newTestimony: TestimonyDetailsDTO? = null,
    val witnessOptions: List<WitnessDTO>? = null,
    val hearingOptions: List<HearingDTO> = listOf(),
    val courtCaseOptions: List<CourtCaseDTO>? = null,
    val contentMode: ContentMode = ContentMode.OVERVIEW,
    val dialogMessage: String? = null,
    val isError: Boolean? = null,

    )