package hu.bme.aut.android.examapp.ui.viewmodel.exam

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.data.room.dto.ExamDto
import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.room.dto.Question
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class ExamEntryViewModel(
    private val examRepository: ExamRepository,
    private val topicRepository: TopicRepository
) : ViewModel(){

    var examUiState by mutableStateOf(ExamUiState())
        private set

    fun updateUiState(examDetails: ExamDetails) {
        examUiState =
            ExamUiState(examDetails = examDetails, isEntryValid = validateInput(examDetails))
    }

    suspend fun saveExam() : Boolean {
        return if (validateInput() && validateUniqueExam()) {
            examRepository.insertExam(examUiState.examDetails.toExam())
            true
        } else {
            examUiState = examUiState.copy(isEntryValid = false)
            false
        }
    }

    /*suspend fun getExamById(id: Int): String {
        return examRepository.getExamById(id).filterNotNull().first().exam
    }*/

    val topicList: StateFlow<List<TopicDto>> = topicRepository.getAllTopics().filterNotNull().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = listOf()
    )

    suspend fun getTopicIdByTopic(topic: String): Int {
        return topicRepository.getTopicByTopic(topic).filterNotNull().first().id
    }

    private fun validateInput(uiState: ExamDetails = examUiState.examDetails): Boolean {
        return with(uiState) {
            Log.d("ExamEntryViewModel", "validateInput: $name, $topicId")
            name.isNotBlank() && topicId != -1
        }
    }

    private suspend fun validateUniqueExam(uiState: ExamDetails = examUiState.examDetails): Boolean {
        return !examRepository.getAllExamName().filterNotNull().first().contains(uiState.name)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class ExamUiState(
    val examDetails: ExamDetails = ExamDetails(),
    val isEntryValid: Boolean = false
)

data class ExamDetails(
    val id: Int = 0,
    val name: String = "",
    val questionList : List<Question> = listOf(),
    val topicId : Int = -1,
    val topicName : String = ""
)

fun ExamDetails.toExam(): ExamDto = ExamDto(
    id = id,
    name = name,
    questionList = questionList.joinToString("¤") { it.toQuestionString() },
    topicId = topicId
)

fun Question.toQuestionString(): String = when(this){
    is TrueFalseQuestionDto -> "${Type.trueFalseQuestion.ordinal}-$id"
    is MultipleChoiceQuestionDto -> "${Type.multipleChoiceQuestion.ordinal}-$id"
    else -> throw IllegalArgumentException("Invalid type")
}

suspend fun ExamDto.toExamUiState(
    isEntryValid: Boolean = false,
    topicName: String,
    questionList: String,
    trueFalseQuestionRepository: TrueFalseQuestionRepository,
    multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository
): ExamUiState = ExamUiState(
    examDetails = this.toExamDetails(topicName, questionList.split("¤").map { it.toQuestion(trueFalseQuestionRepository, multipleChoiceQuestionRepository) }),
    isEntryValid = isEntryValid
)

fun ExamDto.toExamDetails(topicName: String, questionList: List<Question>): ExamDetails = ExamDetails(
    id = id,
    name = name,
    questionList = questionList,
    topicId = topicId,
    topicName = topicName
)

private suspend fun String.toQuestion(
    trueFalseQuestionRepository: TrueFalseQuestionRepository,
    multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository
): Question {
    val question = this.split("-")
    val type = question[0].toInt()
    val questionId = question[1].toInt()
    return when(type){
        Type.trueFalseQuestion.ordinal -> toTrueFalseQuestion(questionId, trueFalseQuestionRepository)
        Type.multipleChoiceQuestion.ordinal -> toMultipleChoiceQuestion(questionId, multipleChoiceQuestionRepository)
        else -> throw IllegalArgumentException("Invalid type")
    }

}

private suspend fun toTrueFalseQuestion(id: Int, trueFalseQuestionRepository: TrueFalseQuestionRepository) : TrueFalseQuestionDto {
    return trueFalseQuestionRepository.getTrueFalseQuestionById(id).filterNotNull().first()
}

private suspend fun toMultipleChoiceQuestion(id: Int, multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository) : MultipleChoiceQuestionDto {
    return multipleChoiceQuestionRepository.getMultipleChoiceQuestionById(id).filterNotNull().first()
}
