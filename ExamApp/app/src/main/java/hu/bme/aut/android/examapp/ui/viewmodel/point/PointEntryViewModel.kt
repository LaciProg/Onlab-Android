package hu.bme.aut.android.examapp.ui.viewmodel.point

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.PointDto

import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
//import hu.bme.aut.android.examapp.data.room.dto.PointDto
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PointEntryViewModel(private val pointRepository: PointRepository) : ViewModel(){

    var pointUiState by mutableStateOf(PointUiState())
        private set

    fun updateUiState(pointDetails: PointDetails) {
        pointUiState =
            PointUiState(pointDetails = pointDetails, isEntryValid = validateInput(pointDetails))
    }

    suspend fun savePoint() : Boolean {
        return if (validateInput() && validateUniqueTopic()) {
            viewModelScope.launch {
                ExamAppApi.retrofitService.postPoint(pointUiState.pointDetails.toPoint())
            }
            //pointRepository.insertPoint(pointUiState.pointDetails.toPoint())
            true
        }
        else{
            pointUiState = pointUiState.copy(isEntryValid = false)
            false
        }
    }

    private fun validateInput(uiState: PointDetails = pointUiState.pointDetails): Boolean {
        return with(uiState) {
            type.isNotBlank() && point.isNotBlank() && goodAnswer.isNotBlank() && badAnswer.isNotBlank()
        }
    }

    private suspend fun validateUniqueTopic(uiState: PointDetails = pointUiState.pointDetails): Boolean {
        return !ExamAppApi.retrofitService.getAllPointName().map{it.name}.contains(uiState.type)
        //return !pointRepository.getAllPointType().filterNotNull().first().contains(uiState.type)
    }

}

data class PointUiState(
    val pointDetails: PointDetails = PointDetails(),
    val isEntryValid: Boolean = false
)

data class PointDetails(
    val id: String = "",
    val point: String = "0",
    val type: String = "",
    val goodAnswer: String = "0",
    val badAnswer: String = "0"
)

fun PointDetails.toPoint(): PointDto = PointDto(
    uuid = id,
    point = point.toDouble(),
    type = type,
    goodAnswer = goodAnswer.toDouble(),
    badAnswer = badAnswer.toDouble()
)

fun PointDto.toPointUiState(isEntryValid: Boolean = false): PointUiState = PointUiState(
    pointDetails = this.toPointDetails(),
    isEntryValid = isEntryValid
)

fun PointDto.toPointDetails(): PointDetails = PointDetails(
    id = uuid,
    point = point.toString(),
    type = type,
    goodAnswer = goodAnswer.toString(),
    badAnswer = badAnswer.toString()
)