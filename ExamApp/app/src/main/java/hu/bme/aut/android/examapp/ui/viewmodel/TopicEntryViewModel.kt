package hu.bme.aut.android.examapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.coroutineContext

class TopicEntryViewModel(private val topicRepository: TopicRepository) : ViewModel(){

    var topicUiState by mutableStateOf(TopicUiState())
        private set

    fun updateUiState(topicDetails: TopicDetails) {
        topicUiState =
            TopicUiState(topicDetails = topicDetails, isEntryValid = validateInput(topicDetails))
    }

    suspend fun saveTopic() {
        if (validateInput()) {
            topicRepository.insertTopic(topicUiState.topicDetails.toTopic())
        }
    }

    private fun validateInput(uiState: TopicDetails = topicUiState.topicDetails): Boolean {
        return with(uiState) {
            topic.isNotBlank() && description.isNotBlank()
        }
    }

}

data class TopicUiState(
    val topicDetails: TopicDetails = TopicDetails(),
    val isEntryValid: Boolean = false
)

data class TopicDetails(
    val id: Int = 0,
    val topic: String = "",
    val parent: String = "",
    val description: String = "",
)

fun TopicDetails.toTopic(): TopicDto = TopicDto(
    id = id,
    topic = topic,
    parentTopic = parent,
    description = description
)

fun TopicDto.toTopicUiState(isEntryValid: Boolean = false): TopicUiState = TopicUiState(
    topicDetails = this.toTopicDetails(),
    isEntryValid = isEntryValid
)

fun TopicDto.toTopicDetails(): TopicDetails = TopicDetails(
    id = id,
    topic = topic,
    parent = parentTopic,
    description = description
)