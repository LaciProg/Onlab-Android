package hu.bme.aut.android.examapp.ui.viewmodel.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.ui.TopicDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TopicDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val topicRepository: TopicRepository,
) : ViewModel() {

    private val topicName: String = checkNotNull(savedStateHandle[TopicDetailsDestination.topicNameArg])

    /**
     * Holds the item details ui state. The data is retrieved from [TopicRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<TopicDetailsUiState> =
        topicRepository.getTopicByTopic(topicName)
            .filterNotNull()
            .map {
                TopicDetailsUiState(topicDetails =  it.toTopicDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TopicDetailsUiState()
            )

    /**
     * Deletes the item from the [TopicRepository]'s data source.
     */
    suspend fun deleteTopic() {
        topicRepository.deleteTopic(uiState.value.topicDetails.toTopic())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicDetailsScreen
 */
data class TopicDetailsUiState(
    val topicDetails: TopicDetails = TopicDetails()
)