package hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.toTrueFalseQuestion
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

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
            multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.correctAnswersList.removeIf(String::isBlank)
            multipleChoiceQuestionRepository.insertMultipleChoiceQuestion(multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.toMultipleChoiceQuestion())
            true
        } else {
            multipleChoiceQuestionUiState = multipleChoiceQuestionUiState.copy(isEntryValid = false)
            false
        }
    }


    private fun validateInput(uiState: MultipleChoiceQuestionDetails = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails): Boolean {
        return with(uiState) {
            question.isNotBlank() && isAnswerChosen && point!= -1 && topic != -1 && !question.contains("/")
        }
    }

    private suspend fun validateUniqueMultipleChoiceQuestion(uiState: MultipleChoiceQuestionDetails = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails): Boolean {
        return !multipleChoiceQuestionRepository.getAllMultipleChoiceQuestionQuestion().filterNotNull().first().contains(uiState.question)
    }

}

data class MultipleChoiceQuestionUiState(
    val multipleChoiceQuestionDetails: MultipleChoiceQuestionDetails = MultipleChoiceQuestionDetails(),
    val isEntryValid: Boolean = false
)

data class MultipleChoiceQuestionDetails(
    val id: Int = 0,
    val question: String = "",
    val answers: MutableList<String> = mutableListOf(""),
    val correctAnswersList: MutableList<String> = mutableListOf(""),
    val point: Int = -1,
    val topic: Int = -1,
    val isAnswerChosen: Boolean = false,
    val pointName: String = "",
    val topicName: String = ""
)

fun MultipleChoiceQuestionDetails.toMultipleChoiceQuestion(): MultipleChoiceQuestionDto{
    var answersConcat = ""
    for (answer in answers){
        answersConcat += answer
        if(answer != answers.last())answersConcat += "¤"
    }

    var correctAnswerConcat = ""
    for (answer in correctAnswersList){
        correctAnswerConcat += answer
        if(answer != correctAnswersList.last())correctAnswerConcat += "¤"
    }

    return MultipleChoiceQuestionDto(
        id = id,
        question = question,
        answers = answersConcat,
        correctAnswersList = correctAnswerConcat,
        point = point,
        topic = topic,
        type = Type.multipleChoiceQuestion.name,
        )
}


fun MultipleChoiceQuestionDto.toMultipleChoiceQuestionUiState(isEntryValid: Boolean = false, pointName: String, topicName: String, isAnswerChosen: Boolean = false): MultipleChoiceQuestionUiState = MultipleChoiceQuestionUiState(
    multipleChoiceQuestionDetails = this.toMultipleChoiceQuestionDetails(pointName = pointName, topicName =  topicName, isAnswerChosen),
    isEntryValid = isEntryValid
)

fun MultipleChoiceQuestionDto.toMultipleChoiceQuestionDetails(pointName: String, topicName: String, isAnswerChosen: Boolean = false): MultipleChoiceQuestionDetails = MultipleChoiceQuestionDetails(
    id = id,
    question = question,
    answers = answers.split("¤").toMutableList(),
    correctAnswersList = correctAnswersList.split("¤").toMutableList(),
    point = point,
    topic = topic,
    pointName = pointName,
    topicName = topicName,
    isAnswerChosen = isAnswerChosen
)

