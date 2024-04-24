package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.TrueFalseQuestionDetailsDestination
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface TrueFalseQuestionDetailsScreenUiState {
    data class Success(val question: TrueFalseQuestionDto) : TrueFalseQuestionDetailsScreenUiState
    data object Error : TrueFalseQuestionDetailsScreenUiState{var errorMessage: String = ""}
    data object Loading : TrueFalseQuestionDetailsScreenUiState
}

class TrueFalseQuestionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val trueFalseQuestionRepository: TrueFalseQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    val trueFalseQuestionId: String = checkNotNull(savedStateHandle[TrueFalseQuestionDetailsDestination.trueFalseQuestionIdArg.toString()])

    var trueFalseDetailsScreenUiState: TrueFalseQuestionDetailsScreenUiState by mutableStateOf(TrueFalseQuestionDetailsScreenUiState.Loading)
    var uiState by mutableStateOf(TrueFalseQuestionDetailsUiState())
    init {
        getQuestion(trueFalseQuestionId)
    }

    fun getQuestion(topicId: String){
        trueFalseDetailsScreenUiState = TrueFalseQuestionDetailsScreenUiState.Loading
        viewModelScope.launch {
            trueFalseDetailsScreenUiState = try{
                val result = ExamAppApi.retrofitService.getTrueFalse(topicId)
                uiState = TrueFalseQuestionDetailsUiState(result.toTrueFalseQuestionDetails(
                    topicName =
                        if (result.topic == "null") ""
                        else ExamAppApi.retrofitService.getTopic(result.topic).topic,
                    pointName =
                        if (result.point == "null") ""
                        else ExamAppApi.retrofitService.getPoint(result.point).type,
                ))
                TrueFalseQuestionDetailsScreenUiState.Success(result)
            } catch (e: IOException) {
                TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Network error"
                TrueFalseQuestionDetailsScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> TrueFalseQuestionDetailsScreenUiState.Error.errorMessage = "Bad request"
                    401 -> TrueFalseQuestionDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> TrueFalseQuestionDetailsScreenUiState.Error.errorMessage = "Content not found"
                    500 -> TrueFalseQuestionDetailsScreenUiState.Error.errorMessage = "Server error"
                    else -> TrueFalseQuestionDetailsScreenUiState.Error
                }
                TrueFalseQuestionDetailsScreenUiState.Error
            }
        }
    }

    suspend fun deleteTrueFalseQuestion() {
        try {
            ExamAppApi.retrofitService.deleteTrueFalse(trueFalseQuestionId)
        } catch (e: IOException) {
            TrueFalseQuestionDetailsScreenUiState.Error.errorMessage = "Network error"
            trueFalseDetailsScreenUiState = TrueFalseQuestionDetailsScreenUiState.Error
        } catch (e: HttpException) {
            when(e.code()){
                400 -> TrueFalseQuestionDetailsScreenUiState.Error.errorMessage = "Cant delete this question because it is used in an exam"
                401 -> TrueFalseQuestionDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> TrueFalseQuestionDetailsScreenUiState.Error.errorMessage = "Content not found"
                500 -> TrueFalseQuestionDetailsScreenUiState.Error.errorMessage = "Server error"
                else -> TrueFalseQuestionDetailsScreenUiState.Error
            }
            trueFalseDetailsScreenUiState = TrueFalseQuestionDetailsScreenUiState.Error
        }
    }

}


data class TrueFalseQuestionDetailsUiState(
    val trueFalseQuestionDetails: TrueFalseQuestionDetails = TrueFalseQuestionDetails()
)