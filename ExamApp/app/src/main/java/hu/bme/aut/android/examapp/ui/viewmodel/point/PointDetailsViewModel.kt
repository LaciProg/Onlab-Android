package hu.bme.aut.android.examapp.ui.viewmodel.point

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.examapp.api.ExamAppApiService
import hu.bme.aut.android.examapp.api.dto.PointDto
import hu.bme.aut.android.examapp.ui.ExamDestination
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed interface PointDetailsScreenUiState {
    data class Success(val point: PointDto) : PointDetailsScreenUiState
    data object Error : PointDetailsScreenUiState{var errorMessage: String = ""}
    data object Loading : PointDetailsScreenUiState
}

@HiltViewModel
class PointDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val retrofitService: ExamAppApiService
) : ViewModel() {

    val pointId: String = checkNotNull(savedStateHandle[ExamDestination.PointDetailsDestination.pointIdArg])

    var pointDetailsScreenUiState: PointDetailsScreenUiState by mutableStateOf(PointDetailsScreenUiState.Loading)

    init {
        getPoint(pointId)
    }

    fun getPoint(pointId: String){
        pointDetailsScreenUiState = PointDetailsScreenUiState.Loading
        viewModelScope.launch {
            pointDetailsScreenUiState = try{
                val result = retrofitService.getPoint(pointId)
                PointDetailsScreenUiState.Success(result)
            } catch (e: IOException) {
                PointDetailsScreenUiState.Error
            } catch (e: HttpException) {
                 when(e.code()){
                     400 -> PointDetailsScreenUiState.Error.errorMessage = "Bad request"
                     401 -> PointDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                     404 -> PointDetailsScreenUiState.Error.errorMessage = "Content not found"
                     500 -> PointDetailsScreenUiState.Error.errorMessage = "Server error"
                     else -> PointDetailsScreenUiState.Error
                 }
                PointDetailsScreenUiState.Error
            }
        }
    }

    suspend fun deletePoint() {
        try{
            viewModelScope.launch {
                retrofitService.deletePoint(pointId)
            }
        } catch (e: IOException) {
            PointDetailsScreenUiState.Error.errorMessage = "Network error"
            pointDetailsScreenUiState = PointDetailsScreenUiState.Error
        } catch (e: HttpException) {
            when(e.code()){
                400 -> PointDetailsScreenUiState.Error.errorMessage = "You can't delete this point because it is used in a question"
                401 -> PointDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> PointDetailsScreenUiState.Error.errorMessage = "Content not found"
                500 -> PointDetailsScreenUiState.Error.errorMessage = "Server error"
                else -> PointDetailsScreenUiState.Error
            }
            pointDetailsScreenUiState = PointDetailsScreenUiState.Error
        }

    }

}

data class PointDetailsUiState(
    val pointDetails: PointDetails = PointDetails()
)