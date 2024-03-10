package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.TopicDetailsDestination
import hu.bme.aut.android.examapp.ui.TrueFalseQuestionDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TrueFalseQuestionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val trueFalseQuestionRepository: TrueFalseQuestionRepository,
) : ViewModel() {

    private val trueFalseQuestionId: String = checkNotNull(savedStateHandle[TrueFalseQuestionDetailsDestination.trueFalseQuestionIdArg.toString()])

    /**
     * Holds the item details ui state. The data is retrieved from [TrueFalseQuestionRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<TrueFalseQuestionDetailsUiState> =
        trueFalseQuestionRepository.getTrueFalseQuestionById(trueFalseQuestionId.toInt())
            .filterNotNull()
            .map {
                TrueFalseQuestionDetailsUiState(topicDetails =  it.toTrueFalseQuestionDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TrueFalseQuestionDetailsUiState()
            )

    /**
     * Deletes the item from the [TrueFalseQuestionRepository]'s data source.
     */
    suspend fun deleteTrueFalseQuestion() {
        trueFalseQuestionRepository.deleteTrueFalseQuestion(uiState.value.topicDetails.toTrueFalseQuestion())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicDetailsScreen
 */
data class TrueFalseQuestionDetailsUiState(
    val topicDetails: TrueFalseQuestionDetails = TrueFalseQuestionDetails()
)