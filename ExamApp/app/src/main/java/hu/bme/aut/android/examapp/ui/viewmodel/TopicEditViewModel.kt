package hu.bme.aut.android.examapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineTopicRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TopicEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val topicRepository: TopicRepository
) : ViewModel() {

    /**
     * Holds current topic ui state
     */
    var topicUiState by mutableStateOf(TopicUiState())
        private set

    private val topicName: String = checkNotNull(savedStateHandle[hu.bme.aut.android.examapp.ui.TopicDetails.topicNameArg])

    init {
        viewModelScope.launch {
            topicUiState = topicRepository.getTopicByTopic(topicName)
                .filterNotNull()
                .first()
                .toTopicUiState(true)
        }
    }

    /**
     * Update the topic in the [TopicRepository]'s data source
     */
    suspend fun updateTopic() {
        if (validateInput(topicUiState.topicDetails)) {
            topicRepository.updateTopic(topicUiState.topicDetails.toTopic())
        }
    }

    /**
     * Updates the [topicUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(topicDetails: TopicDetails) {
        topicUiState =
            TopicUiState(topicDetails = topicDetails, isEntryValid = validateInput(topicDetails))
    }

    private fun validateInput(uiState: TopicDetails = topicUiState.topicDetails): Boolean {
        return with(uiState) {
            topic.isNotBlank() && description.isNotBlank()
        }
    }
}



/**
 * UI state for TopicDetailsScreen
 */
data class TopicDetailsUiState(
    val topicDetails: TopicDetails = TopicDetails()
)