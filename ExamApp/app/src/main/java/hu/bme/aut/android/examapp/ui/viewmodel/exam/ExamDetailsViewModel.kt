package hu.bme.aut.android.examapp.ui.viewmodel.exam

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.ExamDto
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.api.dto.PointDto
import hu.bme.aut.android.examapp.api.dto.Question
import hu.bme.aut.android.examapp.api.dto.TopicDto
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.ExamDestination
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface ExamDetailsScreenUiState {
    data class Success(val exam: ExamDto) : ExamDetailsScreenUiState
    data object Error : ExamDetailsScreenUiState{var errorMessage: String = ""}
    data object Loading : ExamDetailsScreenUiState
}

@HiltViewModel
class ExamDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    val examId: String = checkNotNull(savedStateHandle[ExamDestination.ExamDetailsDestination.examIdArg])

    var examDetailsScreenUiState: ExamDetailsScreenUiState by mutableStateOf(
        ExamDetailsScreenUiState.Loading)
    var uiState by mutableStateOf(ExamDetailsUiState())

    var pointList: List<PointDto> = listOf()
    var topicList: List<TopicDto> = listOf()
    var trueFalseList: List<TrueFalseQuestionDto> = listOf()
    var multipleChoiceList: List<MultipleChoiceQuestionDto> = listOf()
    init {
        getExam(examId)
        try {
            viewModelScope.launch {
                trueFalseList = ExamAppApi.retrofitService.getAllTrueFalse()
                multipleChoiceList = ExamAppApi.retrofitService.getAllMultipleChoice()
                pointList = ExamAppApi.retrofitService.getAllPoint()
                topicList = ExamAppApi.retrofitService.getAllTopic()
            }
        } catch (e: IOException) {
            ExamDetailsScreenUiState.Error.errorMessage = "Network error"
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        } catch (e: HttpException) {
            when(e.code()){
                400 -> ExamDetailsScreenUiState.Error.errorMessage = "Bad request"
                401 -> ExamDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> ExamDetailsScreenUiState.Error.errorMessage = "Content not found"
                500 -> ExamDetailsScreenUiState.Error.errorMessage = "Server error"
                else -> ExamDetailsScreenUiState.Error
            }
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        }
    }


    fun getExam(topicId: String){
        examDetailsScreenUiState = ExamDetailsScreenUiState.Loading
        viewModelScope.launch {
            examDetailsScreenUiState = try{
                val result = ExamAppApi.retrofitService.getExam(topicId)
                uiState = ExamDetailsUiState(result.toExamDetails(
                    topicName =
                    if (result.topicId == "null") ""
                    else ExamAppApi.retrofitService.getTopic(result.topicId).topic,
                    questionList = if(result.questionList == "") listOf() else result.questionList.split("#").map { if(it.toQuestion() != null) it.toQuestion()!! else throw IllegalArgumentException("Invalid type")}
                ))
                ExamDetailsScreenUiState.Success(result)
            } catch (e: IOException) {
                ExamDetailsScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> ExamDetailsScreenUiState.Error.errorMessage = "Bad request"
                    401 -> ExamDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> ExamDetailsScreenUiState.Error.errorMessage = "Content not found"
                    500 -> ExamDetailsScreenUiState.Error.errorMessage = "Server error"
                    else -> ExamDetailsScreenUiState.Error
                }
                ExamDetailsScreenUiState.Error
            } catch (e: IllegalArgumentException){
                ExamDetailsScreenUiState.Error.errorMessage = "Server type"
                ExamDetailsScreenUiState.Error
            }
        }
    }

    private suspend fun String.toQuestion(): Question? {
        val question = this.split("~")
        val type = question[0].toInt()
        val questionId = question[1]
        return when(type){
            Type.trueFalseQuestion.ordinal -> toTrueFalseQuestion(questionId)
            Type.multipleChoiceQuestion.ordinal -> toMultipleChoiceQuestion(questionId)
            else -> throw IllegalArgumentException("Invalid type")
        }

    }

    suspend fun saveQuestionOrdering(list: List<Question>) {
        try{
            ExamAppApi.retrofitService.updateExam(uiState.examDetails.copy(questionList = list).toExam())
        } catch (e: IOException) {
            ExamDetailsScreenUiState.Error.errorMessage = "Network error"
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        } catch (e: HttpException) {
            when(e.code()){
                400 -> ExamDetailsScreenUiState.Error.errorMessage = "Bad request"
                401 -> ExamDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> ExamDetailsScreenUiState.Error.errorMessage = "Content not found"
                500 -> ExamDetailsScreenUiState.Error.errorMessage = "Server error"
                else -> ExamDetailsScreenUiState.Error
            }
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        }
    }

    private suspend fun toTrueFalseQuestion(id: String) : TrueFalseQuestionDto? {
        return try {
            ExamAppApi.retrofitService.getTrueFalse(id)
        } catch (e: IOException) {
            ExamDetailsScreenUiState.Error.errorMessage = "Network error"
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
            null
        } catch (e: HttpException) {
            when(e.code()){
                400 -> ExamDetailsScreenUiState.Error.errorMessage = "Bad request"
                401 -> ExamDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> ExamDetailsScreenUiState.Error.errorMessage = "Content not found"
                500 -> ExamDetailsScreenUiState.Error.errorMessage = "Server error"
                else -> ExamDetailsScreenUiState.Error
            }
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
            null
        }
    }

    private suspend fun toMultipleChoiceQuestion(id: String) : MultipleChoiceQuestionDto? {
        return try {
            ExamAppApi.retrofitService.getMultipleChoice(id)
        } catch (e: IOException) {
            ExamDetailsScreenUiState.Error.errorMessage = "Network error"
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
            null
        } catch (e: HttpException) {
            when(e.code()){
                400 -> ExamDetailsScreenUiState.Error.errorMessage = "Bad request"
                401 -> ExamDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> ExamDetailsScreenUiState.Error.errorMessage = "Content not found"
                500 -> ExamDetailsScreenUiState.Error.errorMessage = "Server error"
                else -> ExamDetailsScreenUiState.Error
            }
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
            null
        }
    }

    suspend fun saveQuestion(ordinal: Int, question: String) {
        examDetailsScreenUiState = ExamDetailsScreenUiState.Loading

        val questionDto: Question = when(ordinal){
            Type.trueFalseQuestion.ordinal -> trueFalseList.first { question == it.question }
            Type.multipleChoiceQuestion.ordinal ->  multipleChoiceList.first { question == it.question }
            else -> throw IllegalArgumentException("Invalid type")
        }

        val newList =  uiState.examDetails.questionList.toMutableList()
        newList.add(questionDto)
        try{
            viewModelScope.launch{
                ExamAppApi.retrofitService.updateExam(uiState.examDetails.copy(questionList = newList).toExam())
                getExam(examId)
                examDetailsScreenUiState = ExamDetailsScreenUiState.Success(ExamAppApi.retrofitService.getExam(examId))
            }
        } catch (e: IOException) {
            ExamDetailsScreenUiState.Error.errorMessage = "Network error"
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        } catch (e: HttpException) {
            when(e.code()){
                400 -> ExamDetailsScreenUiState.Error.errorMessage = "Bad request"
                401 -> ExamDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> ExamDetailsScreenUiState.Error.errorMessage = "Content not found"
                500 -> ExamDetailsScreenUiState.Error.errorMessage = "Server error"
                else -> ExamDetailsScreenUiState.Error
            }
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        }

    }

    suspend fun removeQuestion(question: Question) {
        examDetailsScreenUiState = ExamDetailsScreenUiState.Loading
        val newList =  uiState.examDetails.questionList.toMutableList()

        when(question){
            is TrueFalseQuestionDto -> {
                val remove = trueFalseList.first { it.uuid == question.uuid }
                newList.remove(remove)
            }
            is MultipleChoiceQuestionDto -> {
                val remove = multipleChoiceList.first { it.uuid == question.uuid }
                newList.remove(remove)
            }
        }
        try {
            viewModelScope.launch {
                ExamAppApi.retrofitService.updateExam(uiState.examDetails.copy(questionList = newList).toExam())
                getExam(examId)
                examDetailsScreenUiState = ExamDetailsScreenUiState.Success(ExamAppApi.retrofitService.getExam(examId))
            }
        } catch (e: IOException) {
            ExamDetailsScreenUiState.Error.errorMessage = "Network error"
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        } catch (e: HttpException) {
            when(e.code()){
                400 -> ExamDetailsScreenUiState.Error.errorMessage = "Bad request"
                401 -> ExamDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> ExamDetailsScreenUiState.Error.errorMessage = "Content not found"
                500 -> ExamDetailsScreenUiState.Error.errorMessage = "Server error"
                else -> ExamDetailsScreenUiState.Error
            }
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        }


    }

    suspend fun deleteExam() {
        try{
            ExamAppApi.retrofitService.deleteExam(examId)
        } catch (e: IOException) {
            ExamDetailsScreenUiState.Error.errorMessage = "Network error"
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        } catch (e: HttpException) {
            when(e.code()){
                400 -> ExamDetailsScreenUiState.Error.errorMessage = "Bad request"
                401 -> ExamDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> ExamDetailsScreenUiState.Error.errorMessage = "Content not found"
                500 -> ExamDetailsScreenUiState.Error.errorMessage = "Server error"
                else -> ExamDetailsScreenUiState.Error
            }
            examDetailsScreenUiState = ExamDetailsScreenUiState.Error
        }
    }

}

data class ExamDetailsUiState(
    val examDetails: ExamDetails = ExamDetails()
)