package hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.ui.MultipleChoiceQuestionDetailsDestination
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface MultipleChoiceQuestionEditScreenUiState {
    data class Success(val question: MultipleChoiceQuestionDto) : MultipleChoiceQuestionEditScreenUiState
    data object Error : MultipleChoiceQuestionEditScreenUiState{var errorMessage: String = ""}
    data object Loading : MultipleChoiceQuestionEditScreenUiState
}

class MultipleChoiceQuestionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private lateinit var originalQuestion: String

    var multipleChoiceQuestionUiState by mutableStateOf(MultipleChoiceQuestionUiState())
        private set

    private val multipleChoiceQuestionId: String = checkNotNull(savedStateHandle[MultipleChoiceQuestionDetailsDestination.multipleChoiceQuestionIdArg])

    var multipleChoiceEditScreenUiState: MultipleChoiceQuestionEditScreenUiState by mutableStateOf(
        MultipleChoiceQuestionEditScreenUiState.Loading)

    init {
        getTrueFalseQuestion(multipleChoiceQuestionId)
    }

    fun getTrueFalseQuestion(topicId: String){
        multipleChoiceEditScreenUiState = MultipleChoiceQuestionEditScreenUiState.Loading
        viewModelScope.launch {
            multipleChoiceEditScreenUiState = try{
                val result = ExamAppApi.retrofitService.getMultipleChoice(topicId)
                multipleChoiceQuestionUiState = result.toMultipleChoiceQuestionUiState(isEntryValid = true,
                    topicName =
                    if (result.topic == "null") ""
                    else ExamAppApi.retrofitService.getTopic(result.topic).topic,
                    pointName =
                    if (result.point == "null") ""
                    else ExamAppApi.retrofitService.getPoint(result.point).type,
                    isAnswerChosen = true
                )
                originalQuestion = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.question
                MultipleChoiceQuestionEditScreenUiState.Success(result)
            } catch (e: IOException) {
                MultipleChoiceQuestionEditScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> MultipleChoiceQuestionEditScreenUiState.Error.errorMessage = "Bad request"
                    401 -> MultipleChoiceQuestionEditScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> MultipleChoiceQuestionEditScreenUiState.Error.errorMessage = "Content not found"
                    500 -> MultipleChoiceQuestionEditScreenUiState.Error.errorMessage = "Server error"
                    else -> MultipleChoiceQuestionEditScreenUiState.Error
                }
                MultipleChoiceQuestionEditScreenUiState.Error
            }
        }
    }

    suspend fun updateMultipleChoiceQuestion() : Boolean {
        return if (validateInput(multipleChoiceQuestionUiState.multipleChoiceQuestionDetails) && validateUniqueMultipleChoiceQuestion(multipleChoiceQuestionUiState.multipleChoiceQuestionDetails)) {
            viewModelScope.launch {
                multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.correctAnswersList.removeIf(String::isBlank)
                ExamAppApi.retrofitService.updateMultipleChoice(multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.toMultipleChoiceQuestion())
            }
            true
        }
        else {
            multipleChoiceQuestionUiState = multipleChoiceQuestionUiState.copy(isEntryValid = false)
            false
        }
    }

    fun updateUiState(multipleChoiceQuestionDetails: MultipleChoiceQuestionDetails) {
        multipleChoiceQuestionUiState =
            MultipleChoiceQuestionUiState(multipleChoiceQuestionDetails = multipleChoiceQuestionDetails, isEntryValid = validateInput(multipleChoiceQuestionDetails))
    }

    private fun validateInput(uiState: MultipleChoiceQuestionDetails = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails): Boolean {
        return with(uiState) {
            question.isNotBlank() && isAnswerChosen && point!= "" && topic != "" && !question.contains("/")
        }
    }

    private suspend fun validateUniqueMultipleChoiceQuestion(uiState: MultipleChoiceQuestionDetails = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails): Boolean {
        return !ExamAppApi.retrofitService.getAllMultipleChoiceName().map{it.name}.contains(uiState.question) || originalQuestion == uiState.question
    }
}