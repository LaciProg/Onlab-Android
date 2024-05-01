package hu.bme.aut.android.examapp.ui.viewmodel.point

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.examapp.api.ExamAppApiService
import hu.bme.aut.android.examapp.api.dto.PointDto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface PointEntryScreenUiState {
    data object Success : PointEntryScreenUiState
    data object Error : PointEntryScreenUiState{var errorMessage: String = ""}
    data object Loading : PointEntryScreenUiState
}

@HiltViewModel
class PointEntryViewModel @Inject constructor(val retrofitService: ExamAppApiService): ViewModel(){

    var pointUiState by mutableStateOf(PointUiState())
        private set

    var pointScreenUiState: PointEntryScreenUiState by mutableStateOf(PointEntryScreenUiState.Success)

    fun updateUiState(pointDetails: PointDetails) {
        pointUiState =
            PointUiState(pointDetails = pointDetails, isEntryValid = validateInput(pointDetails))
    }

    suspend fun savePoint() : Boolean {
        return if (validateInput() && validateUniqueTopic()) {
            try{
                viewModelScope.launch {
                    retrofitService.postPoint(pointUiState.pointDetails.toPoint())
                }
                true
            } catch (e: IOException){
                PointEntryScreenUiState.Error.errorMessage = "Network error"
                pointScreenUiState = PointEntryScreenUiState.Error
                false
            } catch (e: HttpException){
                when(e.code()){
                    400 -> PointEntryScreenUiState.Error.errorMessage = "Bad request"
                    401 -> PointEntryScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> PointEntryScreenUiState.Error.errorMessage = "Content not found"
                    500 -> PointEntryScreenUiState.Error.errorMessage = "Server error"
                    else -> PointEntryScreenUiState.Error
                }
                pointScreenUiState = PointEntryScreenUiState.Error
                false
            }
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
        return try{
            !retrofitService.getAllPointName().map{it.name}.contains(uiState.type)
        } catch (e: IOException) {
            PointEntryScreenUiState.Error.errorMessage = "Network error"
            pointScreenUiState = PointEntryScreenUiState.Error
            false
        } catch (e: HttpException){
            when(e.code()){
                400 -> PointEntryScreenUiState.Error.errorMessage = "Bad request"
                401 -> PointEntryScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> PointEntryScreenUiState.Error.errorMessage = "Content not found"
                500 -> PointEntryScreenUiState.Error.errorMessage = "Server error"
                else -> PointEntryScreenUiState.Error
            }
            pointScreenUiState = PointEntryScreenUiState.Error
            false
        }
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