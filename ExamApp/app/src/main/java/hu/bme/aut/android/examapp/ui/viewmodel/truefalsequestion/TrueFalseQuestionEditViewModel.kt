package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.examapp.api.ExamAppApiService
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.ExamDestination
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed interface TrueFalseQuestionEditScreenUiState {
    data class Success(val question: TrueFalseQuestionDto) : TrueFalseQuestionEditScreenUiState
    data object Error : TrueFalseQuestionEditScreenUiState{var errorMessage: String = ""}
    data object Loading : TrueFalseQuestionEditScreenUiState
}

@HiltViewModel
class TrueFalseQuestionEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val retrofitService: ExamAppApiService
) : ViewModel() {

    private lateinit var originalQuestion: String

    var trueFalseQuestionUiState by mutableStateOf(TrueFalseQuestionUiState())
        private set

    private val trueFalseQuestionId: String = checkNotNull(savedStateHandle[ExamDestination.TrueFalseQuestionDetailsDestination.trueFalseQuestionIdArg])

    var trueFalseEditScreenUiState: TrueFalseQuestionEditScreenUiState by mutableStateOf(TrueFalseQuestionEditScreenUiState.Loading)

    init {
        getTrueFalseQuestion(trueFalseQuestionId)
    }

    fun getTrueFalseQuestion(topicId: String){
        trueFalseEditScreenUiState = TrueFalseQuestionEditScreenUiState.Loading
        viewModelScope.launch {
            trueFalseEditScreenUiState = try{
                val result = retrofitService.getTrueFalse(topicId)
                trueFalseQuestionUiState = result.toTrueFalseQuestionUiState(isEntryValid = true,
                    topicName =
                        if (result.topic == "null") ""
                        else retrofitService.getTopic(result.topic).topic,
                    pointName =
                        if (result.point == "null") ""
                        else retrofitService.getPoint(result.point).type,
                    isAnswerChosen = true
                )
                originalQuestion = trueFalseQuestionUiState.trueFalseQuestionDetails.question
                TrueFalseQuestionEditScreenUiState.Success(result)
            } catch (e: IOException) {
                TrueFalseQuestionEditScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Bad request"
                    401 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Content not found"
                    500 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Server error"
                    else -> TrueFalseQuestionEditScreenUiState.Error
                }
                TrueFalseQuestionEditScreenUiState.Error
            }
        }
    }

    suspend fun updateTrueFalseQuestion() : Boolean{
        return if (validateInput(trueFalseQuestionUiState.trueFalseQuestionDetails) && validateUniqueTrueFalseQuestion(trueFalseQuestionUiState.trueFalseQuestionDetails)) {
            try{
                viewModelScope.launch {
                    retrofitService.updateTrueFalse(trueFalseQuestionUiState.trueFalseQuestionDetails.toTrueFalseQuestion())
                }
                return true
            } catch (e: IOException) {
                TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Network error"
                trueFalseEditScreenUiState = TrueFalseQuestionEditScreenUiState.Error
                return false
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Bad request"
                    401 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Content not found"
                    500 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Server error"
                    else -> TrueFalseQuestionEditScreenUiState.Error
                }
                trueFalseEditScreenUiState = TrueFalseQuestionEditScreenUiState.Error
                return false
            }
        }
        else {
            trueFalseQuestionUiState = trueFalseQuestionUiState.copy(isEntryValid = false)
            false
        }
    }


    fun updateUiState(trueFalseQuestionDetails: TrueFalseQuestionDetails) {
        trueFalseQuestionUiState =
            TrueFalseQuestionUiState(trueFalseQuestionDetails = trueFalseQuestionDetails, isEntryValid = validateInput(trueFalseQuestionDetails))
    }

    private fun validateInput(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return with(uiState) {
            question.isNotBlank() && isAnswerChosen && point!= "" && topic != "" && !question.contains("/")
        }
    }

    private suspend fun validateUniqueTrueFalseQuestion(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return try{
            !retrofitService.getAllTrueFalse().map{it.question}.contains(uiState.question) || originalQuestion == uiState.question
        } catch (e: IOException) {
            TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Network error"
            trueFalseEditScreenUiState = TrueFalseQuestionEditScreenUiState.Error
            false
        } catch (e: HttpException) {
            when(e.code()){
                400 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Bad request"
                401 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Content not found"
                500 -> TrueFalseQuestionEditScreenUiState.Error.errorMessage = "Server error"
                else -> TrueFalseQuestionEditScreenUiState.Error
            }
            trueFalseEditScreenUiState = TrueFalseQuestionEditScreenUiState.Error
            false
        }
    }
}



