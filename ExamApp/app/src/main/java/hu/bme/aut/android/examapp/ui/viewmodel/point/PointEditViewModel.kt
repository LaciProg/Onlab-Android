package hu.bme.aut.android.examapp.ui.viewmodel.point

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicDetails
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PointEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val pointRepository: PointRepository
) : ViewModel() {

    var pointUiState by mutableStateOf(PointUiState())
        private set

    private val pointName: String = checkNotNull(savedStateHandle[hu.bme.aut.android.examapp.ui.PointDetailsDestination.pointNameArg])

    init {
        viewModelScope.launch {
            pointUiState = pointRepository.getPointByType(pointName)
                .filterNotNull()
                .first()
                .toPointUiState(true)
        }
    }

    suspend fun updatePoint() : Boolean{
        return if (validateInput(pointUiState.pointDetails) && validateUniqueTopic(pointUiState.pointDetails)) {
            pointRepository.updatePoint(pointUiState.pointDetails.toPoint())
            true
        }
        else{
            pointUiState = pointUiState.copy(isEntryValid = false)
            false
        }
    }


    fun updateUiState(pointDetails: PointDetails) {
        pointUiState =
            PointUiState(pointDetails = pointDetails, isEntryValid = validateInput(pointDetails))
    }

    private fun validateInput(uiState: PointDetails = pointUiState.pointDetails): Boolean {
        return with(uiState) {
            point.isNotBlank() && type.isNotBlank() && goodAnswer.isNotBlank() && badAnswer.isNotBlank() && !type.contains("/")
        }
    }

    private suspend fun validateUniqueTopic(uiState: PointDetails = pointUiState.pointDetails): Boolean {
        return !pointRepository.getAllPointType().filterNotNull().first().contains(uiState.type) || uiState.type == pointName
    }
}



