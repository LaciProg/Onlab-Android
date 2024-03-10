package hu.bme.aut.android.examapp.ui.viewmodel.topic

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TopicEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val topicRepository: TopicRepository
) : ViewModel() {

    private lateinit var originaltopic: String
    /**
     * Holds current topic ui state
     */
    var topicUiState by mutableStateOf(TopicUiState())
        private set

    private val topicId: String = checkNotNull(savedStateHandle[hu.bme.aut.android.examapp.ui.TopicDetailsDestination.topicIdArg.toString()])

    init {
        viewModelScope.launch {
            topicUiState = topicRepository.getTopicById(topicId.toInt())
                .filterNotNull()
                .first()
                .toTopicUiState(true,
                    parentName =  topicRepository.getTopicById(
                        topicRepository.getTopicById(topicId.toInt()).map { it.parentTopic }.first())
                        .map{it.topic}.first())
            originaltopic = topicUiState.topicDetails.topic
        }

    }

    /**
     * Update the topic in the [TopicRepository]'s data source
     */
    suspend fun updateTopic() : Boolean{
        return if (validateInput(topicUiState.topicDetails) && validateUniqueTopic(topicUiState.topicDetails)) {
            topicRepository.updateTopic(topicUiState.topicDetails.toTopic())
            true
        }
        else {
            topicUiState = topicUiState.copy(isEntryValid = false)
            false
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
            topic.isNotBlank() && description.isNotBlank() && !topic.contains("/") && !description.contains("/")
        }
    }

    private suspend fun validateUniqueTopic(uiState: TopicDetails = topicUiState.topicDetails): Boolean {
        return !topicRepository.getAllTopicName().filterNotNull().first().contains(uiState.topic) || originaltopic == uiState.topic
    }
}



