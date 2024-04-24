package hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.launch

class MultipleChoiceQuestionEntryViewModel(private val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository) : ViewModel(){

    var multipleChoiceQuestionUiState by mutableStateOf(MultipleChoiceQuestionUiState())
        private set

    fun updateUiState(multipleChoiceQuestionDetails: MultipleChoiceQuestionDetails) {
        multipleChoiceQuestionUiState =
            MultipleChoiceQuestionUiState(
                multipleChoiceQuestionDetails = multipleChoiceQuestionDetails,
                isEntryValid = validateInput(multipleChoiceQuestionDetails,
                )
            )
    }

    suspend fun saveMultipleChoiceQuestion() : Boolean {
        return if (validateInput() && validateUniqueMultipleChoiceQuestion()) {
            viewModelScope.launch {
                multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.correctAnswersList.removeIf(String::isBlank)
                ExamAppApi.retrofitService.postMultipleChoice(multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.toMultipleChoiceQuestion())
            }
            true
        } else {
            multipleChoiceQuestionUiState = multipleChoiceQuestionUiState.copy(isEntryValid = false)
            false
        }
    }


    private fun validateInput(uiState: MultipleChoiceQuestionDetails = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails): Boolean {
        return with(uiState) {
            question.isNotBlank() && isAnswerChosen && point!= "" && topic != "" && !question.contains("/")
        }
    }

    private suspend fun validateUniqueMultipleChoiceQuestion(uiState: MultipleChoiceQuestionDetails = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails): Boolean {
        return !ExamAppApi.retrofitService.getAllMultipleChoiceName().map{it.name}.contains(uiState.question)
    }

}

data class MultipleChoiceQuestionUiState(
    val multipleChoiceQuestionDetails: MultipleChoiceQuestionDetails = MultipleChoiceQuestionDetails(),
    val isEntryValid: Boolean = false
)

data class MultipleChoiceQuestionDetails(
    val id: String = "",
    val question: String = "",
    val answers: MutableList<String> = mutableListOf(""),
    val correctAnswersList: MutableList<String> = mutableListOf(""),
    val point: String = "",
    val topic: String = "",
    val isAnswerChosen: Boolean = false,
    val pointName: String = "",
    val topicName: String = ""
)

fun MultipleChoiceQuestionDetails.toMultipleChoiceQuestion() = MultipleChoiceQuestionDto(
    uuid = id,
    question = question,
    answers = answers,
    correctAnswersList = correctAnswersList,
    point = point,
    topic = topic,
    type = Type.multipleChoiceQuestion.name,
)



fun MultipleChoiceQuestionDto.toMultipleChoiceQuestionUiState(isEntryValid: Boolean = false, pointName: String, topicName: String, isAnswerChosen: Boolean = false): MultipleChoiceQuestionUiState = MultipleChoiceQuestionUiState(
    multipleChoiceQuestionDetails = this.toMultipleChoiceQuestionDetails(pointName = pointName, topicName =  topicName, isAnswerChosen),
    isEntryValid = isEntryValid
)

fun MultipleChoiceQuestionDto.toMultipleChoiceQuestionDetails(pointName: String, topicName: String, isAnswerChosen: Boolean = false): MultipleChoiceQuestionDetails = MultipleChoiceQuestionDetails(
    id = uuid,
    question = question,
    answers = answers.toMutableList(),
    correctAnswersList = correctAnswersList.toMutableList(),
    point = point,
    topic = topic,
    pointName = pointName,
    topicName = topicName,
    isAnswerChosen = isAnswerChosen
)

