package hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.toTrueFalseQuestion
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MultipleChoiceQuestionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private lateinit var originalQuestion: String
    /**
     * Holds current topic ui state
     */
    var multipleChoiceQuestionUiState by mutableStateOf(MultipleChoiceQuestionUiState())
        private set

    private val multipleChoiceQuestionId: String = checkNotNull(savedStateHandle[hu.bme.aut.android.examapp.ui.MultipleChoiceQuestionDetailsDestination.multipleChoiceQuestionIdArg.toString()])

    init {
        viewModelScope.launch {
            multipleChoiceQuestionUiState = multipleChoiceQuestionRepository.getMultipleChoiceQuestionById(multipleChoiceQuestionId.toInt())
                .filterNotNull()
                .first()
                .toMultipleChoiceQuestionUiState(
                    isEntryValid = true,
                    topicName = topicRepository.getTopicById(multipleChoiceQuestionRepository.getMultipleChoiceQuestionById(multipleChoiceQuestionId.toInt()).map { it.topic }.first()).map { it.topic }.first(),
                    pointName = pointRepository.getPointById(multipleChoiceQuestionRepository.getMultipleChoiceQuestionById(multipleChoiceQuestionId.toInt()).map{ it.point }.first()).map{ it.type }.first(),
                    isAnswerChosen = true
                )
            originalQuestion = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.question
        }

    }

    /**
     * Update the topic in the [MultipleChoiceQuestionRepository]'s data source
     */
    suspend fun updateMultipleChoiceQuestion() : Boolean {
        return if (validateInput(multipleChoiceQuestionUiState.multipleChoiceQuestionDetails) && validateUniqueMultipleChoiceQuestion(multipleChoiceQuestionUiState.multipleChoiceQuestionDetails)) {
            multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.correctAnswersList.removeIf(String::isBlank)
            multipleChoiceQuestionRepository.updateMultipleChoiceQuestion(multipleChoiceQuestionUiState.multipleChoiceQuestionDetails.toMultipleChoiceQuestion())
            true
        }
        else {
            multipleChoiceQuestionUiState = multipleChoiceQuestionUiState.copy(isEntryValid = false)
            false
        }
    }


    /**
     * Updates the [multipleChoiceQuestionUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(multipleChoiceQuestionDetails: MultipleChoiceQuestionDetails) {
        multipleChoiceQuestionUiState =
            MultipleChoiceQuestionUiState(multipleChoiceQuestionDetails = multipleChoiceQuestionDetails, isEntryValid = validateInput(multipleChoiceQuestionDetails))
    }

    private fun validateInput(uiState: MultipleChoiceQuestionDetails = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails): Boolean {
        return with(uiState) {
            question.isNotBlank() && isAnswerChosen && point!= -1 && topic != -1 && !question.contains("/")
        }
    }

    private suspend fun validateUniqueMultipleChoiceQuestion(uiState: MultipleChoiceQuestionDetails = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails): Boolean {
        return !multipleChoiceQuestionRepository.getAllMultipleChoiceQuestionQuestion().filterNotNull().first().contains(uiState.question) || originalQuestion == uiState.question
    }
}



