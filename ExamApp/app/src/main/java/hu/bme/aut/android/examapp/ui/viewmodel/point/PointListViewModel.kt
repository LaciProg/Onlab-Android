package hu.bme.aut.android.examapp.ui.viewmodel.point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PointListViewModel(pointRepository: PointRepository) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [PointRepository] and mapped to
     * [PointListUiState]
     */
    val pointListUiState: StateFlow<PointListUiState> =
        pointRepository.getAllPoints().map { PointListUiState(
            pointList = it.map { pointDto ->
                PointRowUiState(
                    point = pointDto.type,
                    id = pointDto.id
                )
            }
        ) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PointListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for PointListScreen
 */
data class PointListUiState(val pointList: List<PointRowUiState> = listOf(PointRowUiState()))

data class PointRowUiState(
    val point: String = "",
    val id: Int = 0
)