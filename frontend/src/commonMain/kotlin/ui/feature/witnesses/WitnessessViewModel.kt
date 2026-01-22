package ui.feature.witnesses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baze3.models.WitnessDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import service.WitnessService
import ui.common.ContentMode

class WitnessessViewModel(
    private val witnessService: WitnessService = WitnessService(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<WitnessViewState>(WitnessViewState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val res = witnessService.fetchAllWitnesses()
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
            val res = witnessService.fetchWitnessByJmbg(id)
            _uiState.update {
                it.copy(
                    currentDetails = res.data,
                    dialogMessage = if (!res.success) res.message else it.dialogMessage,
                    isError = !res.success,
                    contentMode = ContentMode.OVERVIEW
                )
            }
        }
    }

    fun upsert() {
        viewModelScope.launch {
            val newTestimony = _uiState.value.newDetails ?: return@launch
            val dto = WitnessDTO(
                id = newTestimony.id,
                name = newTestimony.name,
                place = newTestimony.place,
                address = newTestimony.address,
                mothersName = newTestimony.mothersName,
                fathersName = newTestimony.fathersName
            )

            val res = when (_uiState.value.contentMode) {
                ContentMode.EDIT -> witnessService.updateWitness(dto)
                ContentMode.INSERT -> witnessService.createWitness(dto)
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

    fun delete() {
        val current = _uiState.value.currentDetails
        val id = current?.id

        if (id != null) {
            viewModelScope.launch {
                val res = witnessService.deleteWitness(id)
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

    fun update(
        id: String? = null,
        name: String? = null,
        place: String? = null,
        address: String? = null,
        mother: String? = null,
        father: String? = null,
    ) {
        _uiState.update {
            val oldDetails = it.newDetails
            it.copy(
                newDetails = (oldDetails ?: WitnessDTO()).copy(
                    id = id ?: oldDetails?.id,
                    name = name ?: oldDetails?.name,
                    place = place ?: oldDetails?.place,
                    address = address ?: oldDetails?.address,
                    mothersName = mother ?: oldDetails?.mothersName,
                    fathersName = father ?: oldDetails?.fathersName
                )
            )
        }
    }

    fun resetDetails() {
        _uiState.update { it.copy(contentMode = ContentMode.OVERVIEW, newDetails = null, currentDetails = null) }
    }

    fun updateContentMode(contentMode: ContentMode) {
        val newTestimony = when (contentMode) {
            ContentMode.EDIT -> _uiState.value.currentDetails
            ContentMode.OVERVIEW -> _uiState.value.currentDetails
            ContentMode.INSERT -> WitnessDTO()
        }
        _uiState.update { it.copy(contentMode = contentMode, newDetails = newTestimony) }
    }
}

data class WitnessViewState(
    val items: List<WitnessDTO> = listOf(),
    val currentDetails: WitnessDTO? = null,
    val newDetails: WitnessDTO? = null,
    val contentMode: ContentMode = ContentMode.OVERVIEW,
    val dialogMessage: String? = null,
    val isError: Boolean? = null,
)