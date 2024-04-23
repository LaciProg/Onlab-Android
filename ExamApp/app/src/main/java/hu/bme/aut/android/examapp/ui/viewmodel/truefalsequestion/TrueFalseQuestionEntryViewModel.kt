package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
//import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
        return if (validateInput() && validateUniqueTrueFalseQuestion()) {
            viewModelScope.launch {
                ExamAppApi.retrofitService.postTrueFalse(trueFalseQuestionUiState.trueFalseQuestionDetails.toTrueFalseQuestion())
            }
            //trueFalseQuestionRepository.insertTrueFalseQuestion(trueFalseQuestionUiState.trueFalseQuestionDetails.toTrueFalseQuestion())
            true
        }
        else{
            trueFalseQuestionUiState = trueFalseQuestionUiState.copy(isEntryValid = false)
            false
        }
    }

    private fun validateInput(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return with(uiState) {
            question.isNotBlank() && isAnswerChosen && point!= "" && topic != "" && !question.contains("/")
        }
    }

    private suspend fun validateUniqueTrueFalseQuestion(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return !ExamAppApi.retrofitService.getAllTrueFalseName().map{it.name}.contains(uiState.question)
        //return !trueFalseQuestionRepository.getAllTrueFalseQuestionQuestion().filterNotNull().first().contains(uiState.question)
    }

}

data class TrueFalseQuestionUiState(
    val trueFalseQuestionDetails: TrueFalseQuestionDetails = TrueFalseQuestionDetails(),
    val isEntryValid: Boolean = false
)

data class TrueFalseQuestionDetails(
    val id: String = "",
    val question: String = "",
    val correctAnswer: Boolean = false,
    val point: String = "",
    val topic: String = "",
    val isAnswerChosen: Boolean = false,
    val pointName: String = "",
    val topicName: String = ""
)

fun TrueFalseQuestionDetails.toTrueFalseQuestion(): TrueFalseQuestionDto = TrueFalseQuestionDto(
    uuid = id,
    question = question,
    correctAnswer = correctAnswer,
    point = point,
    topic = topic,
    type = Type.trueFalseQuestion.name,
)

fun TrueFalseQuestionDto.toTrueFalseQuestionUiState(isEntryValid: Boolean = false, pointName: String, topicName: String, isAnswerChosen: Boolean = false): TrueFalseQuestionUiState = TrueFalseQuestionUiState(
    trueFalseQuestionDetails = this.toTrueFalseQuestionDetails(pointName = pointName, topicName =  topicName, isAnswerChosen),
    isEntryValid = isEntryValid
)

fun TrueFalseQuestionDto.toTrueFalseQuestionDetails(pointName: String, topicName: String, isAnswerChosen: Boolean = false): TrueFalseQuestionDetails = TrueFalseQuestionDetails(
    id = uuid,
    question = question,
    correctAnswer = correctAnswer,
    point = point,
    topic = topic,
    pointName = pointName,
    topicName = topicName,
    isAnswerChosen = isAnswerChosen
)