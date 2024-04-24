package hu.bme.aut.android.examapp.ui.viewmodel.exam

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.ExamDto
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.ExamDetailsDestination
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionEditScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.toMultipleChoiceQuestionUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ExamEditScreenUiState {
    data class Success(val question: ExamDto) : ExamEditScreenUiState
    object Error : ExamEditScreenUiState{var errorMessage: String = ""}
    object Loading : ExamEditScreenUiState
}


class ExamEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val topicRepository: TopicRepository,
    private val examRepository: ExamRepository,
    private val trueFalseQuestionRepository: TrueFalseQuestionRepository,
    private val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository
) : ViewModel() {

    private lateinit var originalExam: String
    /**
     * Holds current exam ui state
     */
    var examUiState by mutableStateOf(ExamUiState())
        private set

    private val examId: String = checkNotNull(savedStateHandle[ExamDetailsDestination.examIdArg])

    var examEditScreenUiState: ExamEditScreenUiState by mutableStateOf(
        ExamEditScreenUiState.Loading)

    init {
        getExam(examId)
    }

    fun getExam(topicId: String){
        examEditScreenUiState = ExamEditScreenUiState.Loading
        viewModelScope.launch {
            examEditScreenUiState =  try{
                val result = ExamAppApi.retrofitService.getExam(topicId)
                examUiState = result.toExamUiState(isEntryValid = true,
                    topicName =
                    if (result.topicId == "null") ""
                    else ExamAppApi.retrofitService.getTopic(result.topicId).topic,
                    questionList = result.questionList,
                )
                originalExam = examUiState.examDetails.name
                ExamEditScreenUiState.Success(result)
            } catch (e: IOException) {
                ExamEditScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> ExamEditScreenUiState.Error.errorMessage = "Bad request"
                    401 -> ExamEditScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> ExamEditScreenUiState.Error.errorMessage = "Content not found"
                    500 -> ExamEditScreenUiState.Error.errorMessage = "Server error"
                    else -> ExamEditScreenUiState.Error
                }
                ExamEditScreenUiState.Error
            }
        }
    }

    //init {
    //    viewModelScope.launch {
    //        examUiState = examRepository.getExamById(examId.toInt())
    //            .filterNotNull()
    //            .first()
    //            .toExamUiState(true,
    //                topicName = topicRepository.getTopicById(
    //                    examRepository.getExamById(examId.toInt()).map { it.topicId }.first())
    //                    .map{it.topic}.first(),
    //                questionList = examRepository.getExamById(examId.toInt()).map { it.questionList }.first(),
    //                trueFalseQuestionRepository = trueFalseQuestionRepository,
    //                multipleChoiceQuestionRepository = multipleChoiceQuestionRepository
    //            )
    //        originalExam = examUiState.examDetails.name
    //    }
    //}

    /**
     * Update the exam in the [ExamRepository]'s data source
     */
    suspend fun updateExam() : Boolean{
        return if (validateInput(examUiState.examDetails) && validateUniqueExam(examUiState.examDetails)) {
            ExamAppApi.retrofitService.updateExam(examUiState.examDetails.toExam())
            //examRepository.updateExam(examUiState.examDetails.toExam())
            true
        }
        else {
            examUiState = examUiState.copy(isEntryValid = false)
            false
        }
    }

    /**
     * Updates the [examUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(examDetails: ExamDetails) {
        examUiState =
            ExamUiState(examDetails = examDetails, isEntryValid = validateInput(examDetails))
    }

    private fun validateInput(uiState: ExamDetails = examUiState.examDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && topicId != ""
        }
    }

    private suspend fun validateUniqueExam(uiState: ExamDetails = examUiState.examDetails): Boolean {
        return !examRepository.getAllExamName().filterNotNull().first().contains(uiState.name) || originalExam == uiState.name
    }
}



