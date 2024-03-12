package hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.ui.MultipleChoiceQuestionDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MultipleChoiceQuestionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private val multipleChoiceQuestionId: String = checkNotNull(savedStateHandle[MultipleChoiceQuestionDetailsDestination.multipleChoiceQuestionIdArg.toString()])

    /**
     * Holds the item details ui state. The data is retrieved from [MultipleChoiceQuestionRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<MultipleChoiceQuestionDetailsUiState> =
        multipleChoiceQuestionRepository.getMultipleChoiceQuestionById(multipleChoiceQuestionId.toInt())
            .filterNotNull()
            .map { multipleChoiceQuestionDto ->
                MultipleChoiceQuestionDetailsUiState(multipleChoiceQuestionDetails =  multipleChoiceQuestionDto.toMultipleChoiceQuestionDetails(
                    topicName = if(multipleChoiceQuestionDto.topic != -1)
                        topicRepository.getTopicById(multipleChoiceQuestionDto.topic).map{it.topic}.first()
                    else "",
                    pointName = if(multipleChoiceQuestionDto.point != -1)
                    pointRepository.getPointById(multipleChoiceQuestionDto.point).map { it.type }.first()
                    else ""
                ))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MultipleChoiceQuestionDetailsUiState()
            )

    /**
     * Deletes the item from the [MultipleChoiceQuestionRepository]'s data source.
     */
    suspend fun deleteMultipleChoiceQuestion() {
        multipleChoiceQuestionRepository.deleteMultipleChoiceQuestion(uiState.value.multipleChoiceQuestionDetails.toMultipleChoiceQuestion())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicDetailsScreen
 */
data class MultipleChoiceQuestionDetailsUiState(
    val multipleChoiceQuestionDetails: MultipleChoiceQuestionDetails = MultipleChoiceQuestionDetails()
)