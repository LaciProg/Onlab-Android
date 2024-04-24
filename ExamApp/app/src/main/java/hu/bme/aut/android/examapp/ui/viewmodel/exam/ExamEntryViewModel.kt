package hu.bme.aut.android.examapp.ui.viewmodel.exam

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.ExamDto
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.api.dto.Question
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type

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
            ExamAppApi.retrofitService.postExam(examUiState.examDetails.toExam())
            true
        } else {
            examUiState = examUiState.copy(isEntryValid = false)
            false
        }
    }

    suspend fun getTopicIdByTopic(topic: String): String {
        return ExamAppApi.retrofitService.getTopicByTopic(topic)?.uuid ?: ""
    }

    private fun validateInput(uiState: ExamDetails = examUiState.examDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && topicId != ""
        }
    }

    private suspend fun validateUniqueExam(uiState: ExamDetails = examUiState.examDetails): Boolean {
        return !ExamAppApi.retrofitService.getAllExamName().map{it.name}.contains(uiState.name)
    }

}

data class ExamUiState(
    val examDetails: ExamDetails = ExamDetails(),
    val isEntryValid: Boolean = false
)

data class ExamDetails(
    val id: String = "",
    val name: String = "",
    val questionList : List<Question> = listOf(),
    val topicId : String = "",
    val topicName : String = ""
)

fun ExamDetails.toExam(): ExamDto = ExamDto(
    uuid = id,
    name = name,
    questionList = questionList.joinToString("#") { it.toQuestionString() },
    topicId = topicId
)

fun Question.toQuestionString(): String = when(this){
    is TrueFalseQuestionDto -> "${Type.trueFalseQuestion.ordinal}~$uuid"
    is MultipleChoiceQuestionDto -> "${Type.multipleChoiceQuestion.ordinal}~$uuid"
}

suspend fun ExamDto.toExamUiState(
    isEntryValid: Boolean = false,
    topicName: String,
    questionList: String,
): ExamUiState = ExamUiState(
    examDetails = this.toExamDetails(topicName, questionList.split("#").map { it.toQuestion() }),
    isEntryValid = isEntryValid
)

fun ExamDto.toExamDetails(topicName: String, questionList: List<Question>): ExamDetails = ExamDetails(
    id = uuid,
    name = name,
    questionList = questionList,
    topicId = topicId,
    topicName = topicName
)

private suspend fun String.toQuestion(
): Question {
    val question = this.split("~")
    val type = question[0].toInt()
    val questionId = question[1]
    return when(type){
        Type.trueFalseQuestion.ordinal -> toTrueFalseQuestion(questionId)
        Type.multipleChoiceQuestion.ordinal -> toMultipleChoiceQuestion(questionId)
        else -> throw IllegalArgumentException("Invalid type")
    }

}

private suspend fun toTrueFalseQuestion(id: String) : TrueFalseQuestionDto {
    return ExamAppApi.retrofitService.getTrueFalse(id)
}

private suspend fun toMultipleChoiceQuestion(id: String) : MultipleChoiceQuestionDto {
    return ExamAppApi.retrofitService.getMultipleChoice(id)
}
