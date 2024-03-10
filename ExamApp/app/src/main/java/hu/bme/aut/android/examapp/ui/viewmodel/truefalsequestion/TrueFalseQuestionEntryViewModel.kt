package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.data.room.dto.PointDto
import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class TrueFalseQuestionEntryViewModel(private val trueFalseQuestionRepository: TrueFalseQuestionRepository) : ViewModel(){

    var trueFalseQuestionUiState by mutableStateOf(TrueFalseQuestionUiState())
        private set

    fun updateUiState(trueFalseQuestionDetails: TrueFalseQuestionDetails) {
        trueFalseQuestionUiState =
            TrueFalseQuestionUiState(
                trueFalseQuestionDetails = trueFalseQuestionDetails,
                isEntryValid = validateInput(trueFalseQuestionDetails,
                )
            )
    }

    suspend fun saveTrueFalseQuestion() : Boolean {
        return if (validateInput() && validateUniqueTopic()) {
            trueFalseQuestionRepository.insertTrueFalseQuestion(trueFalseQuestionUiState.trueFalseQuestionDetails.toTrueFalseQuestion())
            true
        }
        else{
            trueFalseQuestionUiState = trueFalseQuestionUiState.copy(isEntryValid = false)
            false
        }
    }

    private fun validateInput(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return with(uiState) {
            question.isNotBlank() && isAnswerChosen && point!= -1 && topic != -1 && !question.contains("/")
        }
    }

    private suspend fun validateUniqueTopic(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return !trueFalseQuestionRepository.getAllTrueFalseQuestionQuestion().filterNotNull().first().contains(uiState.question)
    }

}

data class TrueFalseQuestionUiState(
    val trueFalseQuestionDetails: TrueFalseQuestionDetails = TrueFalseQuestionDetails(),
    val isEntryValid: Boolean = false
)

data class TrueFalseQuestionDetails(
    val id: Int = 0,
    val question: String = "",
    val correctAnswer: Boolean = false,
    val point: Int = -1,
    val topic: Int = -1,
    val isAnswerChosen: Boolean = false,
    val pointName: String = "",
    val topicName: String = ""
)

fun TrueFalseQuestionDetails.toTrueFalseQuestion(): TrueFalseQuestionDto = TrueFalseQuestionDto(
    id = id,
    question = question,
    correctAnswer = correctAnswer,
    point = point,
    topic = topic,
    type = Type.trueFalseQuestion.name,
)

fun TrueFalseQuestionDto.toTrueFalseQuestionUiState(isEntryValid: Boolean = false, pointName: String, topicName: String): TrueFalseQuestionUiState = TrueFalseQuestionUiState(
    trueFalseQuestionDetails = this.toTrueFalseQuestionDetails(pointName = pointName, topicName =  topicName),
    isEntryValid = isEntryValid
)

fun TrueFalseQuestionDto.toTrueFalseQuestionDetails(pointName: String, topicName: String): TrueFalseQuestionDetails = TrueFalseQuestionDetails(
    id = id,
    question = question,
    correctAnswer = correctAnswer,
    point = point,
    topic = topic,
    pointName = pointName,
    topicName = topicName
)