package hu.bme.aut.android.examapp.ui.viewmodel.point

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.PointDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface PointEditScreenUiState {
    data class Success(val point: PointDto) : PointEditScreenUiState
    object Error : PointEditScreenUiState{var errorMessage: String = ""}
    object Loading : PointEditScreenUiState
}

class PointEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val pointRepository: PointRepository
) : ViewModel() {

    var pointUiState by mutableStateOf(PointUiState())
        private set

    private val pointId: String = checkNotNull(savedStateHandle[hu.bme.aut.android.examapp.ui.PointDetailsDestination.pointIdArg])

    private lateinit var originalPoint: String

    var pointEditScreenUiState: PointEditScreenUiState by mutableStateOf(PointEditScreenUiState.Loading)


    init {
        //viewModelScope.launch {
        //    pointUiState = pointRepository.getPointById(pointId.toInt())
        //        .filterNotNull()
        //        .first()
        //        .toPointUiState(true)
        //    originalPoint = pointUiState.pointDetails.type
        //}
    }

    init {
        getPoint(pointId)
    }

    fun getPoint(pointId: String){
        pointEditScreenUiState = PointEditScreenUiState.Loading
        viewModelScope.launch {
            pointEditScreenUiState = try{
                val result = ExamAppApi.retrofitService.getPoint(pointId)
                pointUiState = result.toPointUiState(true)
                originalPoint = pointUiState.pointDetails.type
                PointEditScreenUiState.Success(result)
            } catch (e: IOException) {
                PointEditScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> PointEditScreenUiState.Error.errorMessage = "Bad request"
                    401 -> PointEditScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> PointEditScreenUiState.Error.errorMessage = "Content not found"
                    500 -> PointEditScreenUiState.Error.errorMessage = "Server error"
                    else -> PointEditScreenUiState.Error
                }
                PointEditScreenUiState.Error
            }
        }
    }

    suspend fun updatePoint() : Boolean{
        return if (validateInput(pointUiState.pointDetails) && validateUniquePoint(pointUiState.pointDetails)) {
            viewModelScope.launch {
                ExamAppApi.retrofitService.updatePoint(pointUiState.pointDetails.toPoint())
            }
            //pointRepository.updatePoint(pointUiState.pointDetails.toPoint())
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

    private suspend fun validateUniquePoint(uiState: PointDetails = pointUiState.pointDetails): Boolean {
        return !pointRepository.getAllPointType().filterNotNull().first().contains(uiState.type) || originalPoint == uiState.type
    }
}



