package hu.bme.aut.android.examapp.ui.viewmodel.point

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.ui.PointDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PointDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val pointRepository: PointRepository,
) : ViewModel() {

    private val pointId: String = checkNotNull(savedStateHandle[PointDetailsDestination.pointIdArg.toString()])

    val uiState: StateFlow<PointDetailsUiState> =
        pointRepository.getPointById(pointId.toInt())
            .filterNotNull()
            .map {
                PointDetailsUiState(pointDetails =  it.toPointDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PointDetailsUiState()
            )

    suspend fun deletePoint() {
        pointRepository.deletePoint(uiState.value.pointDetails.toPoint())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class PointDetailsUiState(
    val pointDetails: PointDetails = PointDetails()
)