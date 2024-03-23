package hu.bme.aut.android.examapp.ui.viewmodel.exam

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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

    private val examId: String = checkNotNull(savedStateHandle[hu.bme.aut.android.examapp.ui.ExamDetailsDestination.examIdArg.toString()])

    init {
        viewModelScope.launch {
            examUiState = examRepository.getExamById(examId.toInt())
                .filterNotNull()
                .first()
                .toExamUiState(true,
                    topicName = topicRepository.getTopicById(
                        examRepository.getExamById(examId.toInt()).map { it.topicId }.first())
                        .map{it.topic}.first(),
                    questionList = examRepository.getExamById(examId.toInt()).map { it.questionList }.first(),
                    trueFalseQuestionRepository = trueFalseQuestionRepository,
                    multipleChoiceQuestionRepository = multipleChoiceQuestionRepository
                )
            originalExam = examUiState.examDetails.name
        }

    }

    /**
     * Update the exam in the [ExamRepository]'s data source
     */
    suspend fun updateExam() : Boolean{
        return if (validateInput(examUiState.examDetails) && validateUniqueExam(examUiState.examDetails)) {
            examRepository.updateExam(examUiState.examDetails.toExam())
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
            name.isNotBlank() && topicId != -1
        }
    }

    private suspend fun validateUniqueExam(uiState: ExamDetails = examUiState.examDetails): Boolean {
        return !examRepository.getAllExamName().filterNotNull().first().contains(uiState.name) || originalExam == uiState.name
    }
}



