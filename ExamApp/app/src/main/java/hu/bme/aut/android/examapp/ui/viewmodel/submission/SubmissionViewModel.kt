package hu.bme.aut.android.examapp.ui.viewmodel.submission

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
import hu.bme.aut.android.examapp.api.dto.Question
import hu.bme.aut.android.examapp.api.dto.StatisticsDto
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.ExamDestination
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamDetailsUiState
import hu.bme.aut.android.examapp.ui.viewmodel.exam.toExamDetails
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface SubmissionScreenUiState {
    data class Success(val examDto: ExamDto) : SubmissionScreenUiState
    data object Error : SubmissionScreenUiState {var errorMessage: String = ""}
    data object Loading : SubmissionScreenUiState

}

sealed interface SubmissionResultScreenUiState {
    data class Success(val statisticsDto: StatisticsDto) : SubmissionResultScreenUiState
    data object Error : SubmissionResultScreenUiState {var errorMessage: String = ""}
    data object Loading : SubmissionResultScreenUiState
}

@HiltViewModel
class SubmissionViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var submissionScreenUiState: SubmissionScreenUiState by mutableStateOf(SubmissionScreenUiState.Loading)
    var statisticsDialogUiState: SubmissionResultScreenUiState by mutableStateOf(SubmissionResultScreenUiState.Loading)
    val examId: String = checkNotNull(savedStateHandle[ExamDestination.SubmissionDestination.examIdArg])

    var uiState by mutableStateOf(ExamDetailsUiState())

    val answers = Answers(mutableListOf())

    var statisticsDto by mutableStateOf(null as StatisticsDto?)

    init {
        getExam(examId)
    }


    fun getExam(topicId: String){
        submissionScreenUiState = SubmissionScreenUiState.Loading
        viewModelScope.launch {
            submissionScreenUiState = try{
                val result = ExamAppApi.retrofitService.getExam(topicId)
                uiState = ExamDetailsUiState(result.toExamDetails(
                    topicName =
                    if (result.topicId == "null") ""
                    else ExamAppApi.retrofitService.getTopic(result.topicId).topic,
                    questionList = if(result.questionList == "") listOf() else result.questionList.split("#").map { if(it.toQuestion() != null) it.toQuestion()!! else throw IllegalArgumentException("Invalid type")}
                ))
                repeat(uiState.examDetails.questionList.size) {
                    answers.answers.add("")
                }
                SubmissionScreenUiState.Success(result)
            } catch (e: IOException) {
                SubmissionScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> SubmissionScreenUiState.Error.errorMessage = "Bad request"
                    401 -> SubmissionScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> SubmissionScreenUiState.Error.errorMessage = "Content not found"
                    500 -> SubmissionScreenUiState.Error.errorMessage = "Server error"
                    else -> SubmissionScreenUiState.Error
                }
                SubmissionScreenUiState.Error
            } catch (e: IllegalArgumentException){
                SubmissionScreenUiState.Error.errorMessage = "Server type"
                SubmissionScreenUiState.Error
            }
        }
    }

    fun submitAnswers(answers: String): StatisticsDto?{
        statisticsDialogUiState = SubmissionResultScreenUiState.Loading
        viewModelScope.launch {
            try {
                statisticsDto = ExamAppApi.retrofitService.getCorrection(id = examId, answers = answers)
                statisticsDialogUiState = SubmissionResultScreenUiState.Success(statisticsDto!!)
            } catch (e: IOException) {
                SubmissionScreenUiState.Error.errorMessage = "Network error"
                statisticsDialogUiState = SubmissionResultScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> SubmissionScreenUiState.Error.errorMessage = "Bad request"
                    401 -> SubmissionScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> SubmissionScreenUiState.Error.errorMessage = "Content not found"
                    500 -> SubmissionScreenUiState.Error.errorMessage = "Server error"
                    else -> SubmissionScreenUiState.Error
                }
                statisticsDialogUiState = SubmissionResultScreenUiState.Error
            }
        }
        return statisticsDto
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

    private suspend fun toTrueFalseQuestion(id: String) : TrueFalseQuestionDto? {
        return try {
            ExamAppApi.retrofitService.getTrueFalse(id)
        } catch (e: IOException) {
            SubmissionScreenUiState.Error.errorMessage = "Network error"
            submissionScreenUiState = SubmissionScreenUiState.Error
            null
        } catch (e: HttpException) {
            when(e.code()){
                400 -> SubmissionScreenUiState.Error.errorMessage = "Bad request"
                401 -> SubmissionScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> SubmissionScreenUiState.Error.errorMessage = "Content not found"
                500 -> SubmissionScreenUiState.Error.errorMessage = "Server error"
                else -> SubmissionScreenUiState.Error
            }
            submissionScreenUiState = SubmissionScreenUiState.Error
            null
        }
    }

    private suspend fun toMultipleChoiceQuestion(id: String) : MultipleChoiceQuestionDto? {
        return try {
            ExamAppApi.retrofitService.getMultipleChoice(id)
        } catch (e: IOException) {
            SubmissionScreenUiState.Error.errorMessage = "Network error"
            submissionScreenUiState = SubmissionScreenUiState.Error
            null
        } catch (e: HttpException) {
            when(e.code()){
                400 -> SubmissionScreenUiState.Error.errorMessage = "Bad request"
                401 -> SubmissionScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                404 -> SubmissionScreenUiState.Error.errorMessage = "Content not found"
                500 -> SubmissionScreenUiState.Error.errorMessage = "Server error"
                else -> SubmissionScreenUiState.Error
            }
            submissionScreenUiState = SubmissionScreenUiState.Error
            null
        }
    }

}

data class Answers(
    val answers: MutableList<String>
)