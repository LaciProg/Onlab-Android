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
import hu.bme.aut.android.examapp.ui.PointDetailsDestination
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface PointDetailsScreenUiState {
    data class Success(val point: PointDto) : PointDetailsScreenUiState
    object Error : PointDetailsScreenUiState{var errorMessage: String = ""}
    object Loading : PointDetailsScreenUiState
}

class PointDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val pointRepository: PointRepository,
) : ViewModel() {

    val pointId: String = checkNotNull(savedStateHandle[PointDetailsDestination.pointIdArg.toString()])

    var pointDetailsScreenUiState: PointDetailsScreenUiState by mutableStateOf(PointDetailsScreenUiState.Loading)

    init {
        getPoint(pointId)
    }

    fun getPoint(pointId: String){
        pointDetailsScreenUiState = PointDetailsScreenUiState.Loading
        viewModelScope.launch {
            pointDetailsScreenUiState = try{
                val result = ExamAppApi.retrofitService.getPoint(pointId)
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

    //val uiState: StateFlow<PointDetailsUiState> =
    //    pointRepository.getPointById(pointId.toInt())
    //        .filterNotNull()
    //        .map {
    //            PointDetailsUiState(pointDetails =  it.toPointDetails())
    //        }.stateIn(
    //            scope = viewModelScope,
    //            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    //            initialValue = PointDetailsUiState()
    //        )

    suspend fun deletePoint() {
        viewModelScope.launch {
            ExamAppApi.retrofitService.deletePoint(pointId)
        }
        //pointRepository.deletePoint(uiState.value.pointDetails.toPoint())
    }

    companion object {
        var pointDetailsScreenEffected = false
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class PointDetailsUiState(
    val pointDetails: PointDetails = PointDetails()
)