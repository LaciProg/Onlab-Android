package hu.bme.aut.android.examapp.ui.viewmodel.point

import android.net.http.HttpException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.PointDto
//import hu.bme.aut.android.examapp.api.dto.PointDto

import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.ui.PointDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException

sealed interface PointDetailsScreenUiState {
    data class Success(val point: PointDto) : PointDetailsScreenUiState
    object Error : PointDetailsScreenUiState
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
             //try{
                val result = ExamAppApi.retrofitService.getPoint(pointId)
            pointDetailsScreenUiState =  PointDetailsScreenUiState.Success(result)
            //} catch (e: IOException) {
            //    PointDetailsScreenUiState.Error
            //} /*catch (e: HttpException) {
                PointDetailsScreenUiState.Error
            //}
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