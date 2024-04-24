package hu.bme.aut.android.examapp.ui.viewmodel.point

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.NameDto
import hu.bme.aut.android.examapp.api.dto.PointDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListUiState
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionDetailsScreenUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface PointListScreenUiState {
    data class Success(val points: List<NameDto>) : PointListScreenUiState
    object Error : PointListScreenUiState{var errorMessage: String = ""}
    object Loading : PointListScreenUiState
}

class PointListViewModel(pointRepository: PointRepository) : ViewModel() {

    var pointListScreenUiState: PointListScreenUiState by mutableStateOf(PointListScreenUiState.Loading)
    var pointListUiState: PointListUiState by mutableStateOf(PointListUiState())
    init {
        getAllPointList()
    }

    fun getAllPointList(){
        pointListScreenUiState = PointListScreenUiState.Loading
        viewModelScope.launch {
            pointListScreenUiState = try{
                val result = ExamAppApi.retrofitService.getAllPointName()
                pointListUiState = PointListUiState(
                    pointList = result.map { nameDto ->
                        PointRowUiState(
                            point = nameDto.name,
                            id = nameDto.uuid
                        )
                    }
                )
                PointListScreenUiState.Success(result)
            } catch (e: IOException) {
                PointListScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> PointListScreenUiState.Error.errorMessage = "Bad request"
                    401 -> PointListScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> PointListScreenUiState.Error.errorMessage = "Content not found"
                    500 -> PointListScreenUiState.Error.errorMessage = "Server error"
                    else -> PointListScreenUiState.Error
                }
                PointListScreenUiState.Error
            }
        }
    }

    /**
     * Holds home ui state. The list of items are retrieved from [PointRepository] and mapped to
     * [PointListUiState]
     */
    //val pointListUiState: StateFlow<PointListUiState> =
    //    pointRepository.getAllPoints().map { PointListUiState(
    //        pointList = it.map { pointDto ->
    //            PointRowUiState(
    //                point = pointDto.type,
    //                id = pointDto.id
    //            )
    //        }
    //    ) }
    //        .stateIn(
    //            scope = viewModelScope,
    //            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    //            initialValue = PointListUiState()
    //        )

    companion object {
        var pointListScreenEffected = false
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


/**
 * UI state for PointListScreen
 */
data class PointListUiState(val pointList: List<PointRowUiState> = listOf(PointRowUiState()))

data class PointRowUiState(
    val point: String = "",
    val id: String = ""
)