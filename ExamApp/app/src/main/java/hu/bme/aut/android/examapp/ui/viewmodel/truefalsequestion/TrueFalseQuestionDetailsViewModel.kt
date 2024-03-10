package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.TrueFalseQuestionDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TrueFalseQuestionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val trueFalseQuestionRepository: TrueFalseQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private val trueFalseQuestionId: String = checkNotNull(savedStateHandle[TrueFalseQuestionDetailsDestination.trueFalseQuestionIdArg.toString()])

    /**
     * Holds the item details ui state. The data is retrieved from [TrueFalseQuestionRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<TrueFalseQuestionDetailsUiState> =
        trueFalseQuestionRepository.getTrueFalseQuestionById(trueFalseQuestionId.toInt())
            .filterNotNull()
            .map { trueFalseQuestionDto ->
                TrueFalseQuestionDetailsUiState(trueFalseQuestionDetails =  trueFalseQuestionDto.toTrueFalseQuestionDetails(
                    topicName = if(trueFalseQuestionDto.topic != -1)
                        topicRepository.getTopicById(trueFalseQuestionDto.topic).map{it.topic}.first()
                    else "",
                    pointName = if(trueFalseQuestionDto.point != -1)
                    pointRepository.getPointById(trueFalseQuestionDto.point).map { it.type }.first()
                    else ""
                ))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TrueFalseQuestionDetailsUiState()
            )

    /**
     * Deletes the item from the [TrueFalseQuestionRepository]'s data source.
     */
    suspend fun deleteTrueFalseQuestion() {
        trueFalseQuestionRepository.deleteTrueFalseQuestion(uiState.value.trueFalseQuestionDetails.toTrueFalseQuestion())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicDetailsScreen
 */
data class TrueFalseQuestionDetailsUiState(
    val trueFalseQuestionDetails: TrueFalseQuestionDetails = TrueFalseQuestionDetails()
)