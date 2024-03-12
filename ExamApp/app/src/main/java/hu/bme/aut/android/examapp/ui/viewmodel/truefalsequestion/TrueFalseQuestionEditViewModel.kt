package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TrueFalseQuestionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val trueFalseQuestionRepository: TrueFalseQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private lateinit var originalQuestion: String
    /**
     * Holds current topic ui state
     */
    var trueFalseQuestionUiState by mutableStateOf(TrueFalseQuestionUiState())
        private set

    private val trueFalseQuestionId: String = checkNotNull(savedStateHandle[hu.bme.aut.android.examapp.ui.TrueFalseQuestionDetailsDestination.trueFalseQuestionIdArg.toString()])

    init {
        viewModelScope.launch {
            trueFalseQuestionUiState = trueFalseQuestionRepository.getTrueFalseQuestionById(trueFalseQuestionId.toInt())
                .filterNotNull()
                .first()
                .toTrueFalseQuestionUiState(
                    isEntryValid = true,
                    topicName = topicRepository.getTopicById(trueFalseQuestionRepository.getTrueFalseQuestionById(trueFalseQuestionId.toInt()).map { it.topic }.first()).map { it.topic }.first(),
                    pointName = pointRepository.getPointById(trueFalseQuestionRepository.getTrueFalseQuestionById(trueFalseQuestionId.toInt()).map{ it.point }.first()).map{ it.type }.first(),
                    isAnswerChosen = true
                )
            originalQuestion = trueFalseQuestionUiState.trueFalseQuestionDetails.question
        }

    }

    /**
     * Update the topic in the [TrueFalseQuestionRepository]'s data source
     */
    suspend fun updateTrueFalseQuestion() : Boolean{
        return if (validateInput(trueFalseQuestionUiState.trueFalseQuestionDetails) && validateUniqueTrueFalseQuestion(trueFalseQuestionUiState.trueFalseQuestionDetails)) {
            trueFalseQuestionRepository.updateTrueFalseQuestion(trueFalseQuestionUiState.trueFalseQuestionDetails.toTrueFalseQuestion())
            true
        }
        else {
            trueFalseQuestionUiState = trueFalseQuestionUiState.copy(isEntryValid = false)
            false
        }
    }

    /**
     * Updates the [trueFalseQuestionUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(trueFalseQuestionDetails: TrueFalseQuestionDetails) {
        trueFalseQuestionUiState =
            TrueFalseQuestionUiState(trueFalseQuestionDetails = trueFalseQuestionDetails, isEntryValid = validateInput(trueFalseQuestionDetails))
    }

    private fun validateInput(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return with(uiState) {
            question.isNotBlank() && isAnswerChosen && point!= -1 && topic != -1 && !question.contains("/")
        }
    }

    private suspend fun validateUniqueTrueFalseQuestion(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return !trueFalseQuestionRepository.getAllTrueFalseQuestionQuestion().filterNotNull().first().contains(uiState.question) || originalQuestion == uiState.question
    }
}



